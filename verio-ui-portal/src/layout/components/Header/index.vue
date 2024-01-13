<template>
  <div class="header header-light dark-text">
    <div class="container">
      <nav id="navigation" class="navigation navigation-landscape">
        <div class="nav-header">
          <a class="nav-brand" href="/">
            <img src="@/assets/img/logo.png" class="logo" alt=""/>
          </a>
          <div class="nav-toggle"></div>
        </div>
        <div class="nav-menus-wrapper" style="transition-property: none;">
          <ul class="nav-menu">
            <li :class="isActive('/') ? 'active': ''">
              <router-link to="/">首页</router-link>
            </li>
            <li :class="isActive('/house') ? 'active': ''">
              <router-link to="/house">所有房源</router-link>
            </li>
            <li :class="isActive('/post?postType=want_sell') ? 'active': ''">
              <router-link to="/post?postType=want_sell">招租文章</router-link>
            </li>
            <li :class="isActive('/post?postType=want_buy') ? 'active': ''">
              <router-link to="/post?postType=want_buy">求租文章</router-link>
            </li>
            <li :class="isActive('/news') ? 'active': ''">
              <router-link to="/news">新闻资讯</router-link>
            </li>
          </ul>

          <ul v-if="loginUser == null" class="nav-menu nav-menu-social align-to-right">

            <li class="">
              <a href="#" @click="gotoLogin()" class="text-info">
                <i class="fas fa-user-circle mr-1"></i><span class="dn-lg">登录</span>
              </a>
            </li>
            <li>
              <a href="#" @click="gotoRegister()" class="theme-cl">
                <i class="fas fa-arrow-alt-circle-right mr-1"></i>注册
              </a>
            </li>
          </ul>

          <ul v-else class="nav-menu nav-menu-social align-to-right dhsbrd"
              @mouseenter="showDropdown = true"
              @mouseleave="showDropdown = false"
          >

            <li class="">
              <div class="btn-group account-drop show">
                <button type="button" class="btn btn-order-by-filt"
                        data-toggle="dropdown" aria-haspopup="true"
                        aria-expanded="true"
                ><span style="margin-right: 5px;">{{ loginUser.nickName }}</span>
                  <img :src="loginUser.avatar" class="avater-img" alt="">
                </button>
                <div v-show="showDropdown" class="dropdown-menu pull-right animated flipInX show"
                     x-placement="bottom-start"
                     style="position: absolute; width: 130px; transform: translate3d(15px, 59px, 0px); top: 0px; left: 0px; will-change: transform;"
                >
                  <a @click="gotoProfile()"><i class="ti-user"></i>个人信息</a>
                  <a @click="gotoAdmin()"><i class="ti-layers"></i>进入后台</a>
                  <a @click="logout"><i class="ti-unlock"></i>退出登录</a>
                </div>
              </div>
            </li>
          </ul>

        </div>
      </nav>
    </div>
  </div>
</template>


<script>

import { getSimpleInfo } from '@/api/login'

export default {
  components: {},
  data() {
    return {
      showDropdown: false,
      loginUser: null
    }
  },
  created() {
    this.getLoginUser()
  },
  methods: {
    gotoLogin() {
      let redirect = this.$route.path
      this.$router.push('/login?redirect=' + redirect)
    },
    gotoRegister() {
      let redirect = this.$route.path
      this.$router.push('/register?redirect=' + redirect)
    },
    getLoginUser() {
      // 已登录
      if (this.$store.getters.token != undefined && this.$store.getters.token != null) {
        getSimpleInfo().then(res => {
          if (res.user != undefined && res.user != null) {
            this.loginUser = res.user
            this.loginUser.avatar = process.env.VUE_APP_BASE_API + this.loginUser.avatar
          }
        })
      } else {
        this.loginUser = null
      }
    },
    logout() {
      this.$confirm('确定注销并退出系统吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$store.dispatch('LogOut').then(() => {
          this.loginUser = null
        })
      }).catch(() => {
      })
    },
    gotoProfile() {
      window.open(process.env.VUE_APP_MANAGE_URL + '/user/profile')
    },
    gotoAdmin() {
      window.open(process.env.VUE_APP_MANAGE_URL + '/')
    },

    isActive(url) {
      let param = window.location.search
      return this.$route.path == url || (this.$route.path + param) == url
    }

  }
}
</script>

<style scoped>
.pagination-container {
  height: 50px;
}

.nav-menu a {
  font-size: 16px;
}
</style>
