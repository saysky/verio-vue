<template>
  <div>
    <!-- ============================ 最新发布整租 Start ================================== -->
    <section class="gray-simple" style="padding: 40px 0">
      <div class="container">

        <div class="row">
          <div class="col-md-4">

            <el-form ref="elForm" :model="queryParams" size="medium" label-width="90px"
                     style="background: #ffffff;padding: 20px 20px;"
            >
              <el-form-item label="城市名称" prop="city">
                <el-select v-model="queryParams.city" placeholder="请选择城市名称" clearable :style="{width: '100%'}">
                  <el-option v-for="item in cityOptions" :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="小区名称" prop="address">
                <el-input v-model="queryParams.address" placeholder="请输入小区名称" clearable :style="{width: '100%'}">
                </el-input>
              </el-form-item>

              <el-form-item label="卧室数量" prop="bedroomNum">
                <el-select v-model="queryParams.bedroomNum" placeholder="请选择卧室数量" clearable
                           :style="{width: '100%'}"
                >
                  <el-option v-for="item in bedroomNumOptions" :label="item.label" :value="item.value"></el-option>
                </el-select>
              </el-form-item>

              <el-form-item label="是否有电梯" prop="hasElevator">
                <el-select v-model="queryParams.hasElevator" placeholder="是否有电梯" clearable
                           :style="{width: '100%'}"
                >
                  <el-option v-for="item in yesOrNoOptions" :label="item.label" :value="item.value"></el-option>
                </el-select>
              </el-form-item>

              <el-form-item label="是否有空调" prop="hasAirConditioner">
                <el-select v-model="queryParams.hasAirConditioner" placeholder="是否有空调" clearable
                           :style="{width: '100%'}"
                >
                  <el-option v-for="item in yesOrNoOptions" :label="item.label" :value="item.value"></el-option>
                </el-select>
              </el-form-item>

              <el-form-item label="房屋朝向" prop="direction">
                <el-select v-model="queryParams.direction" placeholder="请选择房屋朝向" clearable :style="{width: '100%'}">
                  <el-option v-for="item in directionOptions" :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item>

              <el-form-item label="租金区间">
                <el-col :span="11">
                  <el-form-item prop="params.beginMonthRent">
                    <el-input v-model="queryParams.params.beginMonthRent" placeholder="最小租金" clearable
                              :style="{width: '100%'}"
                    />
                  </el-form-item>
                </el-col>
                <el-col class="line" :span="2"style="text-align: center">-</el-col>
                <el-col :span="11">
                  <el-form-item prop="date2">
                    <el-form-item prop="params.endMonthRent">
                      <el-input v-model="queryParams.params.endMonthRent" placeholder="最大租金" clearable
                                :style="{width: '100%'}"
                      />
                    </el-form-item>
                  </el-form-item>
                </el-col>
              </el-form-item>

              <el-form-item label="面积区间">
                <el-col :span="11">
                  <el-form-item prop="params.beginArea">
                    <el-input v-model="queryParams.params.beginArea" placeholder="最小面积" clearable
                              :style="{width: '100%'}"
                    />
                  </el-form-item>
                </el-col>
                <el-col class="line" :span="2" style="text-align: center">-</el-col>
                <el-col :span="11">
                  <el-form-item prop="date2">
                    <el-form-item prop="params.endArea">
                      <el-input v-model="queryParams.params.endArea" placeholder="最大面积" clearable
                                :style="{width: '100%'}"
                      />
                    </el-form-item>
                  </el-form-item>
                </el-col>
              </el-form-item>

              <el-form-item size="large">
                <el-button type="primary" @click="getList">提交</el-button>
                <el-button @click="resetForm">重置</el-button>
              </el-form-item>
            </el-form>

          </div>
          <div class="col-md-8">
            <div class="row">
              <div class="col-md-6" v-for="item in dataList">
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
              <div class="col-md-12" style="text-align: center;">
                <pagination
                  v-show="total>0"
                  :total="total"
                  :page.sync="queryParams.pageNum"
                  :limit.sync="queryParams.pageSize"
                  :page-sizes="[4, 8, 16, 32]"
                  @pagination="getList"
                />
                <p v-show="total == 0">没有数据</p>
              </div>
            </div>

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
        area: '',
        bedroomNum: '',
        pageNum: 1,
        pageSize: 4,
        params: {}
      },
      cityOptions: ['北京', '上海', '广州', '深圳', '杭州', '南京', '武汉', '成都', '西安', '长沙'],
      directionOptions: ['正南', '正北', '正东', '正西', '东南', '西南', '东北', '西北'],
      dataList: [],
      total: 0,
      bedroomNumOptions: [{
        'label': '一室',
        'value': 1
      }, {
        'label': '二室',
        'value': 2
      }, {
        'label': '三室',
        'value': 3
      }, {
        'label': '四室',
        'value': 4
      }, {
        'label': '五室',
        'value': 5
      }],
      yesOrNoOptions: [{
        'label': '有',
        'value': 1
      }, {
        'label': '无',
        'value': 0
      }]
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
      addMark(this.form).then(response => {
        this.$modal.msgSuccess('收藏成功')
      })
    },
    gotoHouseDetail(id) {
      this.$router.push('/house/detail/' + id)
    },
    resetForm() {
      this.queryParams = {
        address: '',
        city: '',
        moneyRent: '',
        bedroomNum: '',
        pageNum: 1,
        pageSize: 4,
        params: {}
      }
      this.getList();
    }
  },
  mounted() {

  }
}
</script>

<style scoped>
</style>
