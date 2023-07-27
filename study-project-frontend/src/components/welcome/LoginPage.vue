<script setup>
import {Lock, User} from "@element-plus/icons-vue";
import {reactive} from "vue";
import {ElMessage} from "element-plus";
import {get, post} from "@/net";
import router from "@/router";
import {useStore} from "@/stores";

const form = reactive({
  username: '',
  password: '',
  remember: false
})
const store = useStore()

const login = () => {
  if (!form.username || !form.password) {
    ElMessage.warning('请填写用户名和密码！')
  } else {
    post('/api/auth/login', {
      username: form.username,
      password: form.password,
      remember: form.remember
    }, (message) => {
      ElMessage.success(message)
      get('/api/user/me', (message) => {
        store.auth.user = message
        router.push('/index')
      }, () => {
        store.auth.user = null
      })
    })
  }
}
</script>

<template>
  <div style="text-align: center;margin: 0 70px">
    <div style="margin-top: 150px">
      <div style="font-size: 25px">登录</div>
      <div style="font-size: 14px;color: grey">请输入账号密码进行登录</div>
    </div>
    <div style="margin-top: 50px">
      <el-input v-model="form.username" type="text" placeholder="用户名">
        <template #prefix>
          <el-icon>
            <User/>
          </el-icon>
        </template>
      </el-input>

      <el-input v-model="form.password" type="password" style="margin-top: 20px" placeholder="密码">
        <template #prefix>
          <el-icon>
            <Lock/>
          </el-icon>
        </template>
      </el-input>
    </div>
    <div style="margin-top: 10px">
      <el-row>
        <el-col :span="12" style="text-align: left">
          <el-checkbox v-model="form.remember" label="记住我" size="large"/>
        </el-col>
        <el-col :span="12" style="text-align: right">
          <el-link @click="router.push('/forget')">忘记密码？</el-link>
        </el-col>
      </el-row>
    </div>
    <div style="margin-top: 40px">
      <el-button @click="login" type="success" style="width: 300px" plain>立即登录</el-button>
    </div>
    <el-divider>
      <span style="color: grey;font-size: 13px">没有账号</span>
    </el-divider>
    <div>
      <el-button @click="router.push('/register')" type="warning" style="width: 300px" plain>注册账号</el-button>
    </div>
  </div>
</template>

<style scoped>

</style>