<script setup>

import {EditPen, Lock, Message} from "@element-plus/icons-vue";
import {reactive, ref} from "vue";
import {post} from "@/net";
import {ElMessage} from "element-plus";
import router from "@/router";

const form = reactive({
  email: '',
  code: '',
  password: '',
  password_repeat: ''
})

const validatePassword = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次输入密码'))
  } else if (value !== form.password) {
    callback(new Error("两次输入的密码不一致"))
  } else {
    callback()
  }
}

const rules = {
  email: [
    {required: true, message: '请输入邮箱地址', trigger: 'blur'},
    {type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'],},
  ],
  code: [
    {required: true, message: '请输入验证码', trigger: 'blur'},
  ], password: [
    {required: true, message: '请输入密码', trigger: 'blur'},
    {min: 6, max: 16, message: '密码长度必须在6-16个字符之间', trigger: ['blur', 'change']},
  ],
  password_repeat: [
    {validator: validatePassword, trigger: ['blur', 'change']},
  ]
}


const active = ref(0)
const formRef = ref()

const isEmailValid = ref(false)

const coldTime = ref(0)

const onValiddate = (prop, isValid) => {
  if (prop === 'email') {
    isEmailValid.value = isValid
  }
}

const validateEmail = () => {
  coldTime.value = 60
  post('/api/auth/valid-reset-email', {
    email: form.email
  }, (message) => {
    ElMessage.success(message)
    setInterval(() => coldTime.value--, 1000)
  }, (message) => {
    ElMessage.warning(message)
    coldTime.value = 0
  })
}

const startReset = () => {
  formRef.value.validate((isValid) => {
    if (isValid) {
      post('/api/auth/start-reset', {
        email: form.email,
        code: form.code
      }, () => {
        active.value++
      })
    } else {
      ElMessage.warning('请填写电子邮件和验证码')
    }
  })
}

const doReset = () => {
  formRef.value.validate((isValid) => {
    if (isValid) {
      post('/api/auth/do-rest', {
        password: form.password
      }, () => {
        ElMessage.success('修改成功')
        router.push('/')
      })
    } else {
      ElMessage.warning('请填写新密码')
    }
  })
}


</script>

<template>
  <div>
    <div style="margin: 30px 20px">
      <el-steps :active="active" finish-status="success" align-center>
        <el-step title="验证电子邮件"/>
        <el-step title="重新设置密码"/>
      </el-steps>
    </div>

    <transition name="el-fade-in-linear" mode="out-in" style="height: 100%">
      <div v-if="active === 0 " style="text-align: center;margin: 0 70px">
        <div style="margin-top: 100px">
          <div style="font-size: 25px">找回密码</div>
          <div style="font-size: 14px;color: grey;margin-top: 10px">请输入电子邮箱来找回您的密码</div>
          <div style="margin-top: 50px">
            <el-form :model="form" :rules="rules" @validate="onValiddate" ref="formRef">
              <el-form-item prop="email">
                <el-input v-model="form.email" type="email" placeholder="电子邮箱地址">
                  <template #prefix>
                    <el-icon>
                      <Message/>
                    </el-icon>
                  </template>
                </el-input>
              </el-form-item>

              <el-form-item prop="code">
                <el-row :gutter="10">
                  <el-col :span="18" style="text-align: left">
                    <el-input v-model="form.code" :maxlength="6" type="text" placeholder="请输入验证码">
                      <template #prefix>
                        <el-icon>
                          <EditPen/>
                        </el-icon>
                      </template>
                    </el-input>
                  </el-col>
                  <el-col :span="6" style="text-align: right">
                    <el-button @click="validateEmail" type="success" :disabled="!isEmailValid || coldTime > 0">
                      {{ coldTime > 0 ? '请稍等' + coldTime + '秒' : '获取验证码' }}
                    </el-button>
                  </el-col>
                </el-row>
              </el-form-item>
            </el-form>
          </div>
          <div style="margin-top: 70px">
            <el-button @click="startReset()" style="width: 300px" type="danger">立即找回密码</el-button>
          </div>
        </div>
      </div>
    </transition>

    <transition name="el-fade-in-linear" mode="out-in" style="height: 100%">
      <div v-if="active === 1 " style="text-align: center;margin: 0 70px">
        <div style="margin-top: 100px">
          <div style="font-size: 25px">找回密码</div>
          <div style="font-size: 14px;color: grey;margin-top: 10px">请输入新的密码，务必牢记，防止丢失</div>
        </div>
        <div style="margin-top: 50px">
          <el-form :model="form" :rules="rules" @validate="onValiddate" ref="formRef">
            <el-form-item prop="password">
              <el-input type="password" :maxlength="16" v-model="form.password" placeholder="新密码">
                <template #prefix>
                  <el-icon>
                    <Lock/>
                  </el-icon>
                </template>
              </el-input>
            </el-form-item>

            <el-form-item prop="password_repeat">
              <el-input v-model="form.password_repeat" :maxlength="16" type="password" placeholder="重复新密码">
                <template #prefix>
                  <el-icon>
                    <Lock/>
                  </el-icon>
                </template>
              </el-input>
            </el-form-item>
          </el-form>
        </div>
        <div style="margin-top: 70px">
          <el-button @click="doReset()" style="width: 300px" type="danger">确认</el-button>
        </div>
      </div>
    </transition>
  </div>

</template>

<style scoped>

</style>