<template>

  <div id="main-wrapper">
    <Header id="layout-header"/>
    <div class="clearfix"></div>

    <el-row id="layout-middle">
      <Main id="layout-main"/>
    </el-row>
    <Footer id="layout-footer"/>
  </div>
</template>

<script>
import { Header, Main, Footer } from './components'

export default {
  name: 'FrontLayout',
  components: {
    Header,
    Main,
    Footer
  }
}
</script>

<style lang="css" scoped>
@import '~@/assets/css/styles.css';
@import '~@/assets/css/colors.css';
</style>

