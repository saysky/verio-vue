<template>
  <div class="app-container home">
    <el-row :gutter="20">
      <el-col :sm="24" :lg="24">
        <h2>{{ user.nickName }}，你好！欢迎访问房屋租赁系统。</h2>
        <h3>你是 <b>{{ roleGroup }}</b>，具有以下功能：</h3>
        <div v-if="roleGroup == '房东'">
          <p>房屋管理：可以添加和管理您的房屋信息</p>
          <p>订单管理：可以查看您的房屋出租订单信息</p>
          <p>账单管理：可以查看您的房屋的所有账单信息</p>
          <p>报修管理：可以查看和处理租客提交的报修信息</p>
          <p>反馈管理：可以向管理员提交反馈信息</p>
          <p>房屋收藏管理：可以收藏关注一些感兴趣的房屋信息</p>
          <p>文章管理：可以发布招租信息文章</p>
          <p>文章评论管理：可以对文章进行评论和管理您的文章评论</p>
        </div>
        <div v-if="roleGroup == '租客'">
          <p>订单管理：可以查看和处理您的租赁订单信息</p>
          <p>账单管理：可以查看您的所有账单信息</p>
          <p>报修管理：可以向房东提交的报修申请</p>
          <p>反馈管理：可以向管理员提交反馈信息</p>
          <p>房屋收藏管理：可以收藏关注一些感兴趣的房屋信息</p>
          <p>文章管理：可以发布求租文章</p>
          <p>文章评论管理：可以对文章进行评论和管理您的文章评论</p>
        </div>
        <div v-if="roleGroup == '管理员'">
          <p>房屋管理：可以管理所有房屋信息</p>
          <p>订单管理：可以管理所有订单信息</p>
          <p>账单管理：可以管理所有账单信息</p>
          <p>报修管理：可以管理所有报修信息</p>
          <p>反馈管理：可以处理租客和房东提交的反馈信息</p>
          <p>房屋收藏管理：可以收藏关注一些感兴趣的房屋信息</p>
          <p>文章管理：可以管理所有招租信息文章</p>
          <p>文章评论管理：可以管理所有文章评论信息</p>
          <p>用户管理：可以管理所有用户信息</p>
        </div>
        <hr/>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getUserProfile } from '@/api/system/user'

export default {
  name: 'Index',
  data() {
    return {
      // 版本号
      user: [],
      roleGroup: ''
    }
  },
  methods: {},
  created() {
    getUserProfile().then(response => {
      this.user = response.data
      this.roleGroup = response.roleGroup
    })
  }
}
</script>

<style scoped lang="scss">
.home {
  blockquote {
    padding: 10px 20px;
    margin: 0 0 20px;
    font-size: 17.5px;
    border-left: 5px solid #eee;
  }

  hr {
    margin-top: 20px;
    margin-bottom: 20px;
    border: 0;
    border-top: 1px solid #eee;
  }

  .col-item {
    margin-bottom: 20px;
  }

  ul {
    padding: 0;
    margin: 0;
  }

  font-family: "open sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
  font-size: 13px;
  color: #676a6c;
  overflow-x: hidden;

  ul {
    list-style-type: none;
  }

  h4 {
    margin-top: 0px;
  }

  h2 {
    margin-top: 10px;
    font-size: 26px;
    font-weight: 100;
  }

  p {
    margin-top: 10px;

    b {
      font-weight: 700;
    }
  }

  .update-log {
    ol {
      display: block;
      list-style-type: decimal;
      margin-block-start: 1em;
      margin-block-end: 1em;
      margin-inline-start: 0;
      margin-inline-end: 0;
      padding-inline-start: 40px;
    }
  }
}
</style>

