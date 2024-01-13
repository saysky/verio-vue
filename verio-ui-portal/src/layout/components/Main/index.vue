<template>
  <div class="box">
      <router-view :key="$route.fullPath"/>
  </div>
</template>

<script>


export default {
  name: 'Home-Main',
  computed: {
    key() {
      return this.$route.path
    }
  },
  created() {
  }
}
</script>

<style lang="scss" scoped>
</style>
