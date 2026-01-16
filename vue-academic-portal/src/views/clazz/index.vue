<script setup>
/**
 * York University - Course Section Management
 * Refactored for North American Academic Standards
 */
import { onMounted, ref, watch } from 'vue'
import { queryPageApi, addApi, queryInfoApi, updateApi, deleteApi } from '@/api/clazz'
import { queryAllApi as queryAllEmpApi } from '@/api/emp'
import { ElMessage, ElMessageBox } from 'element-plus'

// Academic Programs / Departments (York U Lassonde School of Engineering context)
// York U专业
const subjects = ref([
  { name: 'Computer Science (BSc)', value: 1 },
  { name: 'Software Engineering (BEng)', value: 2 },
  { name: 'Digital Media (BA)', value: 3 },
  { name: 'Information Technology (BCom)', value: 4 },
  { name: 'Computer Engineering (BEng)', value: 5 },
  { name: 'Data Science (BSc)', value: 6 }
])

// Search Form Object
let searchClazz = ref({ begin: '', end: '', date: [], name: '' })

// Table Data Source
let tableData = ref([])

// Lifecycle Hook
onMounted(() => {
  queryPage()
  queryAllEmp()
})

// Staff/Faculty Data
let emps = ref([])

// Load all Faculty/Staff
const queryAllEmp = async () => {
  let result = await queryAllEmpApi()
  if (result.code) {
    emps.value = result.data
  }
}

// Pagination Component State
const pagination = ref({ currentPage: 1, pageSize: 10, total: 0 })

const handleSizeChange = (pageSize) => {
  pagination.value.pageSize = pageSize
  queryPage()
}

const handleCurrentChange = (page) => {
  pagination.value.currentPage = page
  queryPage()
}

// Conditional Paging Query
const queryPage = async () => {
  const result = await queryPageApi(
    searchClazz.value.begin,
    searchClazz.value.end,
    searchClazz.value.name,
    pagination.value.currentPage,
    pagination.value.pageSize
  );

  if (result.code) {
    tableData.value = result.data.rows
    pagination.value.total = result.data.total
  }
}

// Reset Search Filters
const clear = () => {
  searchClazz.value = { begin: '', end: '', date: [], name: '' }
  queryPage()
}

// Watcher for Date Range Picker
watch(() => searchClazz.value.date, (newVal, oldVal) => {
  if (newVal && newVal.length > 0) {
    searchClazz.value.begin = newVal[0]
    searchClazz.value.end = newVal[1]
  } else {
    searchClazz.value.begin = ''
    searchClazz.value.end = ''
  }
})


//----------- Create / Update Logic ---------------------------

let dialogFormVisible = ref(false)
let labelWidth = ref(150) // Increased label width for English text
let formTitle = ref('')

// Course Model
let clazz = ref({
  id: '',
  name: '',       // Course Code/Name (e.g., EECS 1012)
  room: '',       // Lecture Hall
  beginDate: '',  // Term Start
  endDate: '',    // Term End
  subject: '',    // Program
  masterId: ''    // Instructor
})

const clearClazz = () => {
  clazz.value = {
    id: '', name: '', room: '', beginDate: '', endDate: '', subject: '', masterId: ''
  }
}

const addClazz = () => {
  dialogFormVisible.value = true
  formTitle.value = 'Schedule New Course Section'
  clearClazz()
}

const updateClazz = async (id) => {
  clearClazz()
  dialogFormVisible.value = true
  formTitle.value = 'Edit Course Details'

  let result = await queryInfoApi(id)
  if (result.code) {
    clazz.value = result.data
  }
}

// Form Validation Rules
const clazzFormRef = ref()
const rules = ref({
  name: [
    { required: true, message: 'Course Code/Name is required', trigger: 'blur' },
    { min: 4, max: 50, message: 'Length must be 4-50 chars', trigger: 'blur' }
  ],
  room: [
    { min: 1, max: 20, message: 'Length must be 1-20 chars', trigger: 'blur' }
  ],
  beginDate: [{ required: true, message: 'Term start date is required', trigger: 'change' }],
  endDate: [{ required: true, message: 'Term end date is required', trigger: 'change' }],
  subject: [{ required: true, message: 'Program/Major is required', trigger: 'change' }]
})

const resetForm = (clazzForm) => {
  if (!clazzForm) return
  clazzForm.resetFields()
}

// Save Logic
const save = (clazzForm) => {
  if (!clazzForm) return
  clazzForm.validate(async (valid) => {
    if (valid) {
      let api = clazz.value.id ? updateApi(clazz.value) : addApi(clazz.value)
      let result = await api
      if (result.code) {
        ElMessage.success('Operation Successful')
        dialogFormVisible.value = false
        queryPage()
      } else {
        ElMessage.error(result.msg)
      }
    }
  })
}


//------- Delete Logic -------
const delById = async (id) => {
  ElMessageBox.confirm(
    'This will permanently remove this course section. Continue?',
    'Delete Confirmation',
    { confirmButtonText: 'Delete', cancelButtonText: 'Cancel', type: 'warning' }
  ).then(async () => {
    let result = await deleteApi(id)
    if (result.code) {
      ElMessage.success('Deleted Successfully')
      queryPage()
    } else {
      ElMessage.error(result.msg)
    }
  }).catch(() => { })
}
</script>

<template>
  <div class="page-header">
    <div id="title">Course Management</div>
    <div class="subtitle">York University Academic Admin Portal</div>
  </div>

  <el-form :inline="true" :model="searchClazz" class="demo-form-inline">
    <el-form-item label="Course Code">
      <el-input v-model="searchClazz.name" placeholder="e.g. EECS 1012" />
    </el-form-item>

    <el-form-item label="Term Duration">
      <el-date-picker v-model="searchClazz.date" type="daterange" range-separator=" to " start-placeholder="Term Start"
                      end-placeholder="Term End" value-format="YYYY-MM-DD" />
    </el-form-item>

    <el-form-item>
      <el-button type="primary" class="york-btn" @click="queryPage()">Search</el-button>
      <el-button class="york-btn-secondary" @click="clear()">Reset</el-button>
    </el-form-item>
  </el-form>

  <el-button type="danger" class="york-btn-red" @click="addClazz(); resetForm(clazzFormRef)">+ Schedule Course</el-button>
  <br><br>

  <el-table :data="tableData" border style="width: 100%" fit :header-cell-style="{background:'#f5f7fa', color:'#333', fontWeight:'bold'}">
    <el-table-column type="index" label="#" width="55" align="center" />
    <el-table-column prop="name" label="Course Code & Section" align="center" width="220px">
      <template #default="scope">
        <span style="font-weight: 600;">{{ scope.row.name }}</span>
      </template>
    </el-table-column>
    <el-table-column prop="room" label="Lecture Hall" align="center" width="150px" />
    <el-table-column prop="masterName" label="Instructor" align="center" width="180px" />
    <el-table-column prop="beginDate" label="Term Start" align="center" width="150px" />
    <el-table-column prop="endDate" label="Term End" align="center" width="150px" />
    <el-table-column prop="status" label="Status" align="center" width="130px">
      <template #default="scope">
        <el-tag v-if="new Date(scope.row.endDate) < new Date()" type="info">Completed</el-tag>
        <el-tag v-else type="success">Active</el-tag>
      </template>
    </el-table-column>
    <el-table-column prop="updateTime" label="Last Modified" align="center" />
    <el-table-column label="Actions" align="center" width="200px">
      <template #default="scope">
        <el-button type="primary" link size="small"
                   @click="updateClazz(scope.row.id); resetForm(clazzFormRef)">Edit</el-button>
        <el-button type="danger" link size="small" @click="delById(scope.row.id)">Delete</el-button>
      </template>
    </el-table-column>
  </el-table>
  <br>

  <div style="display: flex; justify-content: flex-end;">
    <el-pagination v-model:current-page="pagination.currentPage" v-model:page-size="pagination.pageSize"
                   :page-sizes="[5, 10, 20, 50]" layout="total, sizes, prev, pager, next, jumper"
                   :total="pagination.total" @size-change="handleSizeChange"
                   @current-change="handleCurrentChange" />
  </div>


  <el-dialog v-model="dialogFormVisible" :title="formTitle" width="550px">
    <el-form :model="clazz" ref="clazzFormRef" :rules="rules">
      <el-form-item label="Course Code" :label-width="labelWidth" prop="name">
        <el-input v-model="clazz.name" placeholder="e.g. EECS 1012 Section A" />
      </el-form-item>

      <el-form-item label="Lecture Hall" :label-width="labelWidth" prop="room">
        <el-input v-model="clazz.room" placeholder="e.g. LAS A (Lassonde Bldg)" />
      </el-form-item>

      <el-form-item label="Term Start" :label-width="labelWidth" prop="beginDate">
        <el-date-picker v-model="clazz.beginDate" type="date" placeholder="Select start date" value-format="YYYY-MM-DD"
                        style="width: 100%;" />
      </el-form-item>

      <el-form-item label="Term End" :label-width="labelWidth" prop="endDate">
        <el-date-picker v-model="clazz.endDate" type="date" placeholder="Select end date" value-format="YYYY-MM-DD"
                        style="width: 100%;" />
      </el-form-item>

      <el-form-item label="Instructor" :label-width="labelWidth">
        <el-select v-model="clazz.masterId" placeholder="Select Faculty Member" style="width: 100%;">
          <el-option v-for="emp in emps" :label="emp.name" :value="emp.id" />
        </el-select>
      </el-form-item>

      <el-form-item label="Program" :label-width="labelWidth" prop="subject">
        <el-select v-model="clazz.subject" placeholder="Select Academic Program" style="width: 100%;">
          <el-option v-for="sub in subjects" :label="sub.name" :value="sub.value" />
        </el-select>
      </el-form-item>
    </el-form>

    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogFormVisible = false; resetForm(clazzFormRef)">Cancel</el-button>
        <el-button type="primary" class="york-btn-red" @click="save(clazzFormRef)">Save Course</el-button>
      </span>
    </template>
  </el-dialog>

</template>

<style scoped>
/* York University Brand Colors */
/* York Red: #E31837 */

#title {
  font-size: 26px;
  font-weight: 700;
  color: #333;
  margin-bottom: 5px;
  border-left: 5px solid #E31837; /* York Red Accent */
  padding-left: 15px;
}

.subtitle {
  font-size: 14px;
  color: #666;
  margin-left: 20px;
  margin-bottom: 25px;
}

/* Custom Button Styles to match York U */
.york-btn {
  background-color: #333; /* Dark Grey for search */
  border-color: #333;
}
.york-btn:hover {
  background-color: #555;
  border-color: #555;
}

.york-btn-red {
  background-color: #E31837; /* York Red */
  border-color: #E31837;
}
.york-btn-red:hover {
  background-color: #C41230; /* Darker Red */
  border-color: #C41230;
}

.york-btn-secondary {
  background-color: #f5f5f5;
  color: #333;
  border: 1px solid #dcdfe6;
}
.york-btn-secondary:hover {
  background-color: #fff;
  border-color: #c0c4cc;
  color: #E31837;
}
</style>