<template>
  <div>
    <!-- ============================ Hero Banner Start================================== -->
    <div class="image-cover hero_banner"
         data-overlay="1"
    >
      <div class="container">

        <h1 class="big-header-capt mb-0">开始一段美好的生活吧！</h1>
        <p class="text-center mb-5">搜索你所在的城市的小区或房屋类型吧！</p>
        <!-- Type -->
        <div class="full_search_box nexio_search lightanic_search hero_search-radius modern">
          <div class="search_hero_wrapping">
            <form>
              <div class="row">
                <div class="col-lg-3 col-md-3 col-sm-12">
                  <div class="form-group">
                    <label>小区名称</label>
                    <div class="input-with-icon">
                      <input type="text" v-model="queryParams.address" class="form-control">
                    </div>
                  </div>
                </div>

                <div class="col-lg-2 col-md-2 col-sm-12">
                  <div class="form-group">
                    <label>城市名称</label>
                    <div class="input-with-icon">
                      <select class="form-control" v-model="queryParams.city" style="height: 35px">
                        <option value="">不限</option>
                        <option :value="item" v-for="item in cityList">{{ item }}</option>
                      </select>
                    </div>
                  </div>
                </div>


                <div class="col-lg-2 col-md-2 col-sm-12">
                  <div class="form-group">
                    <label>卧室数量</label>
                    <div class="input-with-icon">
                      <select class="form-control" v-model="queryParams.bedroomNum" style="height: 35px">
                        <option value="">不限</option>
                        <option value="1">一室</option>
                        <option value="2">二室</option>
                        <option value="3">三室</option>
                        <option value="4">四室</option>
                        <option value="5">五室</option>
                      </select>
                    </div>
                  </div>
                </div>

                <div class="col-lg-3 col-md-3 col-sm-12">
                  <div class="form-group">
                    <label>月租金范围</label>
                    <div class="input-with-icon">
                      <select class="form-control" v-model="queryParams.moneyRent" style="height: 35px">
                        <option value="">不限</option>
                        <option value="0;999">1000以内</option>
                        <option value="1000;1999">1000-1999</option>
                        <option value="2000;3999">2000-3999</option>
                        <option value="4000;5999">4000-5999</option>
                        <option value="6000;7999">6000-7999</option>
                        <option value="8000;9999">8000-9999</option>
                        <option value="10000;14999">10000-14999</option>
                        <option value="15000;19999">15000-19999</option>
                      </select>
                    </div>
                  </div>
                </div>

                <div class="col-lg-2 col-md-2 col-sm-12 small-padd">
                  <div class="form-group none">
                    <button type="button" class="btn search-btn" @click="getList()">搜索</button>
                  </div>
                </div>

              </div>
            </form>


          </div>
        </div>
      </div>
    </div>
    <!-- ============================ Hero Banner End ================================== -->

    <!-- ============================ 最新发布整租 Start ================================== -->
    <section class="gray-simple">
      <div class="container">

        <div class="row justify-content-center">
          <div class="col-lg-7 col-md-8">
            <div class="sec-heading center">
              <h2>最新房屋信息</h2>
              <p>给你推荐了一些最新的发布的房屋信息</p>
            </div>
          </div>
        </div>

        <div class="row">
          <!-- Single Property -->
          <div class="col-lg-4 col-md-6 col-sm-12" v-for="item in dataList">
            <div class="single_property_style property_style_2 modern">

              <div class="listing_thumb_wrapper">
                <div class="property_gallery_slide-thumb">
                  <a @click="gotoHouseDetail(item.id)">
                    <img :src="item.thumbnailUrl" class="img-fluid mx-auto" alt=""></a>
                </div>
              </div>

              <div class="property_caption_wrappers pb-0">
                <div class="property_short_detail">
                  <div class="pr_type_status">
                    <h4 class="pr-property_title mb-1">
                      <a @click="gotoHouseDetail(item.id)">
                        {{ item.title }}
                      </a>
                    </h4>
                    <div class="listing-location-name">
                      <a @click="gotoHouseDetail(item.id)">
                        {{ item.address }}
                      </a>
                    </div>
                  </div>
                  <div class="property-real-price">
                    <a @click="gotoHouseDetail(item.id)" class="cl-blue">
                      ¥ {{ item.monthRent }}<span class="price_status">/月</span>
                    </a>
                  </div>
                </div>
              </div>

              <div class="modern_property_footer">
                <div class="property-lists flex-1">
                  <ul>
                    <li>
                      <span class="flatcons">
                          <img src="@/assets/img/bed.svg" alt="">
                      </span>
                      {{ item.bedroomNum }} 卧室
                    </li>
                    <li>
                      <span class="flatcons">
                        <img src="@/assets/img/bath.svg" alt="">
                      </span>{{ item.toiletNum }} 卫生间
                    </li>
                  </ul>
                </div>
                <div class="fp_types">
                  <a class="markHouse" @click="handleMark(item.id)">收藏</a>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="row">
          <div class="col-lg-12 col-md-12 col-sm-12 text-center">
            <a style="color: #ffffff" class="btn btn-theme arrow-btn bg-2" @click="gotoHouseList">
              查看更多
              <span><i class="ti-arrow-right"></i></span>
            </a>
          </div>
        </div>

      </div>
    </section>
    <div class="clearfix"></div>
  </div>
</template>

<script>
import { portalHouseList } from '@/api/house'
import { addMark } from '@/api/mark'

export default {
  components: {},
  data() {
    return {
      queryParams: {
        address: '',
        city: '',
        moneyRent: '',
        bedroomNum: '',
        pageNum: 1,
        pageSize: 6,
        params: {}
      },
      cityList: ['北京', '上海', '广州', '深圳', '杭州', '南京', '武汉', '成都', '西安', '长沙'],
      dataList: [],
      total: 0
    }
  },
  created() {
    const categoryId = this.$route.params && this.$route.params.categoryId
    const tags = this.$route.params && this.$route.params.tags
    const title = this.$route.params && this.$route.params.title
    this.queryParams.categoryId = categoryId
    this.queryParams.tags = tags
    this.queryParams.title = title
    this.getList()
  },
  methods: {
    goHome() {
      this.$router.push('/')
    },
    // 获取分页列表
    getList() {
      if (this.queryParams.moneyRent != '') {
        let moneyRents = this.queryParams.moneyRent.split(';')
        this.queryParams.params['beginMonthRent'] = moneyRents[0]
        this.queryParams.params['endMonthRent'] = moneyRents[1]
      } else {
        this.queryParams.params['beginMonthRent'] = null
        this.queryParams.params['endMonthRent'] = null
      }
      portalHouseList(this.queryParams).then(res => {
        this.dataList = res.rows
        this.dataList.forEach(item => {
          item.thumbnailUrl = process.env.VUE_APP_BASE_API + item.thumbnailUrl
        })
        this.total = res.total
      })
    },
    // 监听 pageSize 改变的事件
    handleSizeChange(newSize) {
      this.queryParams.pageSize = newSize
      this.getList()
    },
    // 监听页码值改变的事件
    handleCurrentChange(newPage) {
      this.queryParams.pageNum = newPage
      this.getList()
    },
    handleMark(id) {
      let markForm = {
        houseId: id
      }
      addMark(markForm).then(response => {
        this.$modal.msgSuccess('收藏成功')
      })
    },
    gotoHouseDetail(id) {
      this.$router.push('/house/detail/' + id)
    },
    gotoHouseList() {
      this.$router.push('/house')
    }
  },
  mounted() {
    // window.eventBus.$on('getArticleList', (data) => {
    //   this.queryParams.title = data.title
    //   this.queryParams.tags = data.tags
    //   this.queryParams.categoryId = data.categoryId
    //   this.getList()
    // })
  }
}
</script>

<style scoped>
.pagination-container {
  height: 50px;
}

.image-cover.hero_banner {
  background: url('~@/assets/img/banner-2.png') no-repeat center center;
}
</style>
