<script setup>

import {get} from "@/net";
import {ElMessage} from "element-plus";
import router from "@/router";
import {useStore} from "@/stores";

const store = useStore()

const logout = () => {
  get('/api/auth/logout', () => {
    ElMessage.success('再见！')
    store.auth.user = null
    router.push('/')
  })
}

</script>

<template>
  <div>
    欢迎进入平台
  </div>
  <div>
    <el-button @click="logout()" type="danger" plain>退出登录</el-button>
  </div>
</template>

<style scoped>

</style>