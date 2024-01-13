<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="房屋编号" prop="id">
        <el-input
          v-model="queryParamsTemp.idStr"
          placeholder="请输入房屋编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="房屋名称" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入房屋名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="房屋状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择房屋状态" clearable>
          <el-option
            v-for="dict in dict.type.house_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['verio:house:add']"
        >新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['verio:house:edit']"
        >修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['verio:house:remove']"
        >删除
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="houseList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="发布人" align="center">
        <template slot-scope="scope">
          {{ scope.row.user ? scope.row.user.nickName : '' }}
        </template>
      </el-table-column>
      <el-table-column label="房屋编号" align="center" width="180px">
        <template slot-scope="scope">
          <a @click="gotoHouseDetail(scope.row.id)" class="text-success">FW{{ scope.row.id }}</a>
        </template>
      </el-table-column>
      <el-table-column label="缩略图" align="center" prop="thumbnailUrl" width="100">
        <template slot-scope="scope">
          <image-preview :src="scope.row.thumbnailUrl" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="房屋名称" align="center" prop="title"/>
      <el-table-column label="月租金/元" align="center" prop="monthRent"/>
      <el-table-column label="房屋状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.house_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['verio:house:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['verio:house:remove']"
          >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改房屋对话框 -->
    <el-dialog :title="title" :close-on-click-modal="false" :visible.sync="open" width="1000px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-position="top">
        <el-form-item label="房屋名称" prop="title">
          <el-input v-model="form.title" placeholder="请输入房屋名称"/>
        </el-form-item>
        <el-form-item label="详细描述内容">
          <editor v-model="form.content" :min-height="192"/>
        </el-form-item>
        <el-row>
          <el-col :span="5">
            <el-form-item label="城市" prop="city">
              <el-select v-model="form.city" placeholder="请选择">
                <el-option
                  v-for="item in cityList"
                  :key="item"
                  :label="item"
                  :value="item"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="11" offset="1">
            <el-form-item label="详细地址(完整地址，包括小区名)" prop="address">
              <el-input v-model="form.address" placeholder="请输入详细地址"/>
            </el-form-item>
          </el-col>
          <el-col :span="5" offset="1">
            <el-form-item label="经纬度(可用GPS软件定位)" prop="longitudeLatitude">
              <el-input v-model="form.longitudeLatitude" placeholder="格式：116.405419,39.916263"/>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="5">
            <el-form-item label="房产证号" prop="certificateNo">
              <el-input v-model="form.certificateNo" placeholder="请输入房产证号"/>
            </el-form-item>
          </el-col>
          <el-col :span="5" offset="1">
            <el-form-item label="面积/㎡" prop="area">
              <el-input type="number" v-model="form.area" placeholder="请输入面积"/>
            </el-form-item>
          </el-col>
          <el-col :span="5" offset="1">
            <el-form-item label="是否有电梯" prop="hasElevator">
              <el-radio-group v-model="form.hasElevator">
                <el-radio
                  v-for="dict in dict.type.has_elevator"
                  :key="dict.value"
                  :label="parseInt(dict.value)"
                >{{ dict.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="5" offset="1">
            <el-form-item label="是否有空调" prop="hasAirConditioner">
              <el-radio-group v-model="form.hasAirConditioner">
                <el-radio
                  v-for="dict in dict.type.has_air_conditioner"
                  :key="dict.value"
                  :label="parseInt(dict.value)"
                >{{ dict.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="5">
            <el-form-item label="当前所在楼层数" prop="floor">
              <el-input type="number" v-model="form.floor" placeholder="请输入当前所在楼层数"/>
            </el-form-item>
          </el-col>
          <el-col :span="5" offset="1">
            <el-form-item label="房子最大楼层数" prop="maxFloor">
              <el-input type="number" v-model="form.maxFloor" placeholder="请输入房子最大楼层数"/>
            </el-form-item>
          </el-col>
          <el-col :span="5" offset="1">
            <el-form-item label="朝向" prop="direction">
              <el-select v-model="form.direction" placeholder="请选择">
                <el-option
                  v-for="item in directionList"
                  :key="item"
                  :label="item"
                  :value="item"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="5" offset="1">
            <el-form-item label="建成年份" prop="buildYear">
              <el-input type="number" v-model="form.buildYear" placeholder="请输入建成年份"/>
            </el-form-item>
          </el-col>
          <el-col :span="5" offset="1">

          </el-col>
        </el-row>

        <el-row>
          <el-col :span="5">
            <el-form-item label="卧室数量" prop="bedroomNum">
              <el-input type="number" v-model="form.bedroomNum" placeholder="请输入卧室数量"/>
            </el-form-item>
          </el-col>
          <el-col :span="5" offset="1">
            <el-form-item label="客厅数量" prop="livingRoomNum">
              <el-input type="number" v-model="form.livingRoomNum" placeholder="请输入客厅数量"/>
            </el-form-item>
          </el-col>
          <el-col :span="5" offset="1">
            <el-form-item label="厨房数量" prop="kitchenNum">
              <el-input type="number" v-model="form.kitchenNum" placeholder="请输入厨房数量"/>
            </el-form-item>
          </el-col>
          <el-col :span="5" offset="1">
            <el-form-item label="卫生间数量" prop="toiletNum">
              <el-input type="number" v-model="form.toiletNum" placeholder="请输入卫生间数量"/>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="5">
            <el-form-item label="月租金/元" prop="monthRent">
              <el-input type="number" v-model="form.monthRent" placeholder="请输入"/>
            </el-form-item>
          </el-col>
          <el-col :span="5" offset="1">
            <el-form-item label="联系人姓名" prop="contactName">
              <el-input v-model="form.contactName" placeholder="请输入联系人姓名"/>
            </el-form-item>
          </el-col>
          <el-col :span="5" offset="1">
            <el-form-item label="联系人手机" prop="contactPhone">
              <el-input v-model="form.contactPhone" placeholder="请输入联系人手机"/>
            </el-form-item>
          </el-col>
          <el-col :span="5" offset="1">

          </el-col>
        </el-row>


        <el-row>
          <el-col :span="16">
            <el-form-item label="轮播图URL" prop="slideUrl">
              <image-upload v-model="form.slideUrl" limit="5"/>
            </el-form-item>
          </el-col>
          <el-col :span="7" offset="1">
            <el-form-item label="缩略图URL" prop="thumbnailUrl">
              <image-upload v-model="form.thumbnailUrl" limit="1"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="房屋状态" prop="hasElevator">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in dict.type.house_status"
              :key="dict.value"
              :label="parseInt(dict.value)"
            >{{ dict.label }}
            </el-radio>
          </el-radio-group>
        </el-form-item>

        <br>
        注意：<a href="https://api.map.baidu.com/lbsapi/getpoint/index.html?qq-pf-to=pcqq.c2c" target="_blank">经纬度拾取参考</a>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listHouse, getHouse, delHouse, addHouse, updateHouse } from '@/api/verio/house'
import { isNumberStr } from '@/utils'

export default {
  name: 'House',
  dicts: ['house_status', 'has_elevator', 'has_air_conditioner'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 房屋表格数据
      houseList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      directionList: ['正南', '正北', '正东', '正西', '东南', '西南', '东北', '西北'],
      cityList: ['北京','上海', '广州', '深圳', '杭州', '南京', '武汉', '成都', '西安', '长沙'],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: null,
        status: null
      },
      queryParamsTemp: {
        idStr: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        createTime: [
          { required: true, message: '创建时间不能为空', trigger: 'blur' }
        ],
        userId: [
          { required: true, message: '房东用户ID不能为空', trigger: 'blur' }
        ],
        title: [
          { required: true, message: '房屋名称不能为空', trigger: 'blur' }
        ],
        content: [
          { required: true, message: '详细描述内容不能为空', trigger: 'blur' }
        ],
        city: [
          { required: true, message: '城市名称不能为空', trigger: 'blur' }
        ],
        address: [
          { required: true, message: '详细地址不能为空', trigger: 'blur' }
        ],
        thumbnailUrl: [
          { required: true, message: '缩略图URL不能为空', trigger: 'blur' }
        ],
        slideUrl: [
          { required: true, message: '轮播图URL不能为空', trigger: 'blur' }
        ],
        monthRent: [
          { required: true, message: '月租金额不能为空', trigger: 'blur' }
        ],
        status: [
          { required: true, message: '房屋状态不能为空', trigger: 'change' }
        ],
        certificateNo: [
          { required: true, message: '房产证号不能为空', trigger: 'blur' }
        ],
        toiletNum: [
          { required: true, message: '卫生间数量不能为空', trigger: 'blur' }
        ],
        kitchenNum: [
          { required: true, message: '厨房数量不能为空', trigger: 'blur' }
        ],
        livingRoomNum: [
          { required: true, message: '客厅数量不能为空', trigger: 'blur' }
        ],
        bedroomNum: [
          { required: true, message: '卧室数量不能为空', trigger: 'blur' }
        ],
        hasAirConditioner: [
          { required: true, message: '是否有空调不能为空', trigger: 'change' }
        ],
        hasElevator: [
          { required: true, message: '是否有电梯不能为空', trigger: 'change' }
        ],
        area: [
          { required: true, message: '面积不能为空', trigger: 'blur' }
        ],
        floor: [
          { required: true, message: '当前所在楼层数不能为空', trigger: 'blur' }
        ],
        maxFloor: [
          { required: true, message: '房子最大楼层数不能为空', trigger: 'blur' }
        ],
        buildYear: [
          { required: true, message: '建成年份不能为空', trigger: 'blur' }
        ],
        direction: [
          { required: true, message: '朝向不能为空', trigger: 'blur' }
        ],
        contactName: [
          { required: true, message: '联系人姓名不能为空', trigger: 'blur' }
        ],
        contactPhone: [
          { required: true, message: '联系人手机不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询房屋列表 */
    getList() {
      this.loading = true
      if (this.queryParamsTemp.idStr) {
        this.queryParams.id = this.queryParamsTemp.idStr.replace('FW', '')
        if (!isNumberStr(this.queryParams.id)) {
          this.$modal.msgError('房屋编号格式不正确')
          return
        }
      } else {
        this.queryParams.id = null
      }
      listHouse(this.queryParams).then(response => {
        this.houseList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        createTime: null,
        userId: null,
        title: null,
        content: null,
        city: null,
        address: null,
        thumbnailUrl: null,
        slideUrl: null,
        monthRent: null,
        status: 0,
        certificateNo: null,
        toiletNum: null,
        kitchenNum: null,
        livingRoomNum: null,
        bedroomNum: null,
        hasAirConditioner: null,
        area: null,
        floor: null,
        maxFloor: null,
        hasElevator: null,
        buildYear: null,
        direction: null,
        longitudeLatitude: null,
        contactName: null,
        contactPhone: null
      }
      this.resetForm('form')
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm('queryForm')
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '添加房屋'
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getHouse(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改房屋'
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateHouse(this.form).then(response => {
              this.$modal.msgSuccess('修改成功')
              this.open = false
              this.getList()
            })
          } else {
            addHouse(this.form).then(response => {
              this.$modal.msgSuccess('新增成功')
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm('是否确认删除房屋编号为"' + ids + '"的数据项？').then(function() {
        return delHouse(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {
      })
    },
    gotoHouseDetail(id) {
      window.open(process.env.VUE_APP_PORTAL_URL + '/house/detail/' + id)
    }
  }
}
</script>
