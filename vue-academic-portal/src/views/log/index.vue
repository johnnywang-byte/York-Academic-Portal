<script setup>
/**
 * System Log Component
 * Adapted for Portfolio Demo
 * Displays operational logs for auditing and debugging
 */
import { onMounted, ref } from 'vue'
import { queryPageApi } from '@/api/log'

// Table Data Source
// 列表展示数据
let tableData = ref([])

// Lifecycle Hook
// 钩子函数 - 页面加载时触发
onMounted(() => {
  queryPage()
})

// Pagination Configuration
// 分页组件
const pagination = ref({ currentPage: 1, pageSize: 15, total: 0 })

// Handle Page Size Change
// 每页展示记录数发生变化时触发
const handleSizeChange = (pageSize) => {
  pagination.value.pageSize = pageSize
  queryPage()
}

// Handle Page Change
// 当前页码发生变化时触发
const handleCurrentChange = (page) => {
  pagination.value.currentPage = page
  queryPage()
}

// Fetch Logs
// 分页条件查询
const queryPage = async () => {
  const result = await queryPageApi(pagination.value.currentPage, pagination.value.pageSize);

  if (result.code) {
    tableData.value = result.data.rows
    pagination.value.total = result.data.total
  }
}
</script>

<template>

  <div>
    <div id="title">System Operations Log</div>
    <br>
  </div>

  <el-table :data="tableData" border style="width: 100%" fit size="small">

    <el-table-column prop="operateEmpName" label="Operator" align="center" width="100px" />

    <el-table-column prop="operateTime" label="Timestamp" align="center" width="160px" />

    <el-table-column prop="className" label="Class Name" align="center" width="280px">
      <template #default="scope">
        <span style="font-size: 12px;">{{ scope.row.className }}</span>
      </template>
    </el-table-column>

    <el-table-column prop="methodName" label="Method" align="center" width="120px" />

    <el-table-column prop="costTime" label="Duration (ms)" align="center" width="120px">
      <template #default="scope">
        <span :style="{ color: scope.row.costTime > 1000 ? 'red' : 'inherit' }">
          {{ scope.row.costTime }}ms
        </span>
      </template>
    </el-table-column>

    <el-table-column prop="methodParams" label="Request Params" align="center" width="280px">
      <template #default="scope">
        <el-popover effect="dark" trigger="hover" placement="top" width="400px" popper-style="font-size:12px; word-break: break-all;">
          <template #default>
            <div><strong>Full Params:</strong><br/>{{ scope.row.methodParams }}</div>
          </template>
          <template #reference>
            <el-tag v-if="scope.row.methodParams && scope.row.methodParams.length <= 30" type="info" size="small">
              {{ scope.row.methodParams }}
            </el-tag>
            <el-tag v-else type="info" size="small">
              {{ scope.row.methodParams ? scope.row.methodParams.substring(0, 30) + '...' : 'Empty' }}
            </el-tag>
          </template>
        </el-popover>
      </template>
    </el-table-column>

    <el-table-column prop="returnValue" label="Return Value" align="center">
      <template #default="scope">
          <span style="font-size: 12px; color: #666; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;">
            {{ scope.row.returnValue }}
          </span>
      </template>
    </el-table-column>
  </el-table>
  <br>

  <el-pagination v-model:current-page="pagination.currentPage" v-model:page-size="pagination.pageSize"
                 :page-sizes="[10, 20, 50, 100]" layout="total, sizes, prev, pager, next, jumper" :total="pagination.total"
                 @size-change="handleSizeChange" @current-change="handleCurrentChange" />
</template>


<style scoped>
#title {
  font-size: 24px;
  font-weight: 600;
  color: #333;
  margin-bottom: 10px;
}
</style>