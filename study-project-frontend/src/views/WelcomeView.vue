<script setup>

</script>

<template>
  <div style="width: 100vw;height: 100vh;overflow: hidden;display: flex">
    <div style="flex: 1">
      <el-image style="width: 100%;height: 100%" fit="cover"
                src="https://ts1.cn.mm.bing.net/th/id/R-C.86e96697c02fba3eacedcefcef0706c5?rik=PUtHbMfc5OSzGw&riu=http%3a%2f%2fpic.zsucai.com%2ffiles%2f2013%2f0802%2fwmdqfj1.jpg&ehk=Kp9VFd8k2hu9ivnX1DS1%2fMENcrnPnqi%2bYeABzCExyLs%3d&risl=&pid=ImgRaw&r=0"/>
      <div class="welcome-title">
        <div style="font-size: 30px;font-weight: bold">欢迎进入前后端分离项目</div>
        <div style="margin-top: 10px">在这里你可以学习到第一个前后端分离项目</div>
        <div style="margin-top: 5px">本项目采用Java boot3+Security+vue3+Element Plus等技术的项目</div>
      </div>
    </div>
    <div style="width: 500px;background: white;z-index: 1">
      <router-view v-slot="{ Component }">
        <transition name="el-fade-in-linear" mode="out-in">
          <component :is="Component" style="height: 100%" />
        </transition>
      </router-view>

    </div>
  </div>
</template>

<style scoped>
.welcome-title {
  position: absolute;
  bottom: 30px;
  left: 30px;
  color: white;
  text-shadow: 0 0 10px black;
}

</style>