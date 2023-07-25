<script setup>

import {EditPen, Lock, Message, User} from "@element-plus/icons-vue";
import router from "@/router";
import {reactive} from "vue";
const form = reactive({
  username: '',
  password: '',
  password_repeat: '',
  email: '',
  code: ''
})
</script>

<template>
  <div style="text-align: center;margin: 0 70px">
    <div style="margin-top: 150px">
      <div style="font-size: 25px">注册新用户</div>
      <div style="font-size: 14px;color: grey">欢迎进入注册页面，请输出信息开始注册吧！</div>
    </div>
    <div style="margin-top: 50px">
      <el-input v-model="form.username" type="text" placeholder="用户名">
        <template #prefix>
          <el-icon>
            <User/>
          </el-icon>
        </template>
      </el-input>

      <el-input type="password" v-model="form.password" style="margin-top: 10px" placeholder="密码">
        <template #prefix>
          <el-icon>
            <Lock/>
          </el-icon>
        </template>
      </el-input>

      <el-input v-model="form.password_repeat" type="password" style="margin-top: 10px" placeholder="重复密码">
        <template #prefix>
          <el-icon>
            <Lock/>
          </el-icon>
        </template>
      </el-input>
    </div>
    <div style="margin-top: 10px">
      <el-input v-model="form.email" type="email" placeholder="电子邮箱地址">
        <template #prefix>
          <el-icon>
            <Message/>
          </el-icon>
        </template>
      </el-input>
    </div>

    <div style="margin-top: 10px">
      <el-row :gutter="10">
        <el-col :span="17" style="text-align: left">
          <el-input v-model="form.code" type="text" placeholder="请输入验证码">
            <template #prefix>
              <el-icon>
                <EditPen/>
              </el-icon>
            </template>
          </el-input>
        </el-col>
        <el-col :span="6" style="text-align: right">
          <el-button type="success">获取验证码</el-button>
        </el-col>
      </el-row>
    </div>

    <div style="margin-top: 40px">
      <el-button  type="warning" style="width: 300px" plain>立即注册</el-button>
    </div>

    <div style="font-size: 14px;margin-top: 20px">
      <el-space style="color: grey">已有账号？</el-space><el-link @click="router.push('/')" type="primary" style="translate: 0 -2px">立即登录</el-link>
    </div>

  </div>
</template>

<style scoped>

</style>