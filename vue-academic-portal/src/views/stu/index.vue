<script setup>
/**
 * York University - Undergrad Student Records
 * Refactored for North American Academic Standards
 * Updated to match Backend Schema (yearLevel, enrollmentStatus, gpa)
 */
import { onMounted, ref } from 'vue'
import { queryPageApi, addApi, queryInfoApi, updateApi, deleteApi, handleViolationApi } from '@/api/stu'
import { queryAllApi as queryAllClazzApi } from '@/api/clazz'
import { ElMessage, ElMessageBox } from 'element-plus'

// -------- Data Definitions (York U Logic) --------

// Year Level Options
const yearLevels = ref([
  { name: '1st Year', value: 1 },
  { name: '2nd Year', value: 2 },
  { name: '3rd Year', value: 3 },
  { name: '4th Year', value: 4 },
  { name: "Graduate", value: 5 },
  { name: 'Alumni', value: 6 }
])

// Gender Options
const genders = ref([
  { name: 'Male', value: 1 },
  { name: 'Female', value: 2 }
])

// Enrollment Status Options
const enrollmentStatusOptions = ref([
  { name: 'Full-time', value: 1 },
  { name: 'Part-time', value: 0 }
])

// Search Form Model
// 🚨 Refactored: degree -> yearLevel
let searchStu = ref({ clazzId: '', yearLevel: '', name: '' })

// Table Data Source
let tableData = ref([])

// Lifecycle Hook
onMounted(() => {
  queryPage()
  queryAllClazz() // Load Majors
})

// Majors List
let majors = ref([])

// Load Major Options
const queryAllClazz = async () => {
  let result = await queryAllClazzApi()
  if (result.code) {
    majors.value = result.data
  }
}

// Bulk Selection
let selectIds = ref([])
const handleSelectionChange = (selection) => {
  selectIds.value = selection.map(item => item.id)
}

// Pagination
const pagination = ref({ currentPage: 1, pageSize: 10, total: 0 })

const handleSizeChange = (pageSize) => {
  pagination.value.pageSize = pageSize
  queryPage()
}

const handleCurrentChange = (page) => {
  pagination.value.currentPage = page
  queryPage()
}

// Execute Query
const queryPage = async () => {
  const result = await queryPageApi(
    searchStu.value.clazzId,
    searchStu.value.yearLevel, // 🚨 Pass yearLevel
    searchStu.value.name,
    pagination.value.currentPage,
    pagination.value.pageSize
  );

  if (result.code) {
    tableData.value = result.data.rows
    pagination.value.total = result.data.total
  }
}

const clear = () => {
  searchStu.value = { clazzId: '', yearLevel: '', name: '', }
  queryPage()
}


//----------- Create / Update Logic ---------------------------

let dialogFormVisible = ref(false)
let labelWidth = ref(140)
let formTitle = ref('')

// Student Model
// 🚨 Refactored: Properties match Java Entity exactly now
let stu = ref({
  id: '',
  name: '',
  no: '',
  gender: '',
  phone: '',
  idCard: '',
  enrollmentStatus: 1, // Default Full-time
  address: '',
  yearLevel: '',
  graduationDate: '',
  clazzId: ''
})

const clearStu = () => {
  stu.value = {
    id: '', name: '', no: '', gender: '', phone: '', idCard: '',
    enrollmentStatus: 1, address: '', yearLevel: '', graduationDate: '', clazzId: ''
  }
}

const addStu = () => {
  dialogFormVisible.value = true
  formTitle.value = 'Register New Student'
  clearStu()
}

const updateStu = async (id) => {
  clearStu()
  dialogFormVisible.value = true
  formTitle.value = 'Update Student Record'
  let result = await queryInfoApi(id)
  if (result.code) {
    stu.value = result.data
  }
}

// Validation Rules
const stuFormRef = ref()
const rules = ref({
  name: [
    { required: true, message: 'Full name is required', trigger: 'blur' },
    { min: 2, max: 40, message: 'Length must be 2-40 chars', trigger: 'blur' }
  ],
  no: [
    { required: true, message: 'Student Number is required', trigger: 'blur' },
    { pattern: /^\d{9}$/, message: 'Student Number must be 9 digits', trigger: 'blur' }
  ],
  gender: [{ required: true, message: 'Gender is required', trigger: 'change' }],
  phone: [
    { required: true, message: 'Phone is required', trigger: 'blur' },
    { pattern: /^\d{10}$/, message: 'Format: 10 digits (e.g. 4165550199)', trigger: 'blur' }
  ],
  idCard: [
    { required: true, message: 'SIN / Govt ID is required', trigger: 'blur' },
    { min: 9, max: 20, message: 'ID length must be 9-20 characters', trigger: 'blur' }
  ],
  // 🚨 Refactored validation keys
  enrollmentStatus: [
    { required: true, message: 'Enrollment status is required', trigger: 'change' }
  ],
  yearLevel: [
    { required: true, message: 'Year Level is required', trigger: 'change' }
  ],
  clazzId: [
    { required: true, message: 'Major is required', trigger: 'change' }
  ]
})

const resetForm = (stuForm) => {
  if (!stuForm) return
  stuForm.resetFields()
}

// Save Logic
const save = (stuForm) => {
  if (!stuForm) return
  stuForm.validate(async (valid) => {
    if (valid) {
      let api = stu.value.id ? updateApi(stu.value) : addApi(stu.value)
      let result = await api
      if (result.code) {
        ElMessage.success('Record Saved Successfully')
        dialogFormVisible.value = false
        queryPage()
      } else {
        ElMessage.error(result.msg)
      }
    }
  })
}


//------- Action Logic -------

const delById = async (id) => {
  ElMessageBox.confirm(
    'This action will permanently remove the student record. Confirm?',
    'Delete Record',
    { confirmButtonText: 'Delete', cancelButtonText: 'Cancel', type: 'warning' }
  ).then(async () => {
    let result = await deleteApi(`${id}`)
    if (result.code) {
      ElMessage.success('Record Deleted')
      queryPage()
    } else {
      ElMessage.error(result.msg)
    }
  }).catch(() => {})
}

const delByIds = async () => {
  ElMessageBox.confirm(
    'Delete all selected student records?',
    'Batch Delete',
    { confirmButtonText: 'Delete All', cancelButtonText: 'Cancel', type: 'warning' }
  ).then(async () => {
    let result = await deleteApi(selectIds.value.join(','))
    if (result.code) {
      ElMessage.success('Batch Deletion Complete')
      queryPage()
    } else {
      ElMessage.error(result.msg)
    }
  }).catch(() => {})
}

// Update GPA Logic
const openViolation = (id) => {
  ElMessageBox.prompt('Enter New GPA (0.0 - 9.0):', 'Update Student GPA', {
    confirmButtonText: 'Update',
    cancelButtonText: 'Cancel',
    // Regex for decimal number
    inputPattern: /^(?:[0-8](?:\.\d+)?|9(?:\.0+)?)$/,
    inputErrorMessage: 'GPA must be between 0.0 and 9.0',
    inputType: 'text'
  }).then(async (val) => {
    // 🚨 Calls the refactored /gpa API
    let result = await handleViolationApi(id, val.value);
    if (result.code) {
      ElMessage.success('GPA Updated Successfully')
      queryPage()
    } else {
      ElMessage.error(result.msg)
    }
  })
}
</script>

<template>
  <div class="page-container">
    <div id="title">Undergrad Student Records</div>

    <div class="search-bar">
      <el-form :inline="true" :model="searchStu" class="demo-form-inline">
        <el-form-item label="Student Name">
          <el-input v-model="searchStu.name" placeholder="Enter name" />
        </el-form-item>

        <el-form-item label="Year Level">
          <el-select v-model="searchStu.yearLevel" placeholder="All Years">
            <el-option v-for="level in yearLevels" :label="level.name" :value="level.value" />
          </el-select>
        </el-form-item>

        <el-form-item label="Course">
          <el-select v-model="searchStu.clazzId" placeholder="Select Course">
            <el-option v-for="major in majors" :label="major.name" :value="major.id" />
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" class="york-btn" @click="queryPage()">Search Records</el-button>
          <el-button @click="clear()">Clear</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="toolbar">
      <el-button type="danger" class="york-btn-red" @click="addStu(); resetForm(stuFormRef)">
        + Register Student
      </el-button>
      <el-button plain type="danger" @click="delByIds()">
        - Batch Remove
      </el-button>
    </div>

    <el-table :data="tableData" border style="width: 100%; margin-top: 20px;" fit @selection-change="handleSelectionChange"
              :header-cell-style="{background:'#f5f7fa', color:'#333', fontWeight:'bold'}">
      <el-table-column type="selection" align="center" width="40" />

      <el-table-column prop="name" label="Student Name" align="left" width="140px">
        <template #default="scope">
          <span style="font-weight: 600; color: #E31837;">{{ scope.row.name }}</span>
        </template>
      </el-table-column>

      <el-table-column prop="no" label="Student Number" align="center" width="140px" />

      <el-table-column prop="clazzName" label="Course" align="center" width="180px" />

      <el-table-column prop="gender" label="Gender" align="center" width="80px">
        <template #default="scope">
          {{ scope.row.gender == 1 ? 'M' : 'F' }}
        </template>
      </el-table-column>

      <el-table-column prop="yearLevel" label="Year Level" align="center" width="110px">
        <template #default="scope">
          <el-tag v-if="scope.row.yearLevel == 1" type="info">1st Year</el-tag>
          <el-tag v-else-if="scope.row.yearLevel == 2" type="primary">2nd Year</el-tag>
          <el-tag v-else-if="scope.row.yearLevel == 3" type="success">3rd Year</el-tag>
          <el-tag v-else-if="scope.row.yearLevel == 4" type="warning">4th Year</el-tag>
          <el-tag v-else type="danger">Grad</el-tag>
        </template>
      </el-table-column>

      <el-table-column prop="gpa" label="GPA (9.0)" align="center" width="100px">
        <template #default="scope">
          <span style="font-weight: bold;">
            {{ scope.row.gpa }}
          </span>
        </template>
      </el-table-column>

      <el-table-column prop="credits" label="Credits" align="center" width="100px">
        <template #default="scope">
          {{ scope.row.credits }}
        </template>
      </el-table-column>

      <el-table-column prop="updateTime" label="Last Updated" align="center" width="160px">
        <template #default="scope">
          {{ scope.row.updateTime ? scope.row.updateTime.substring(0,10) : '-' }}
        </template>
      </el-table-column>

      <el-table-column label="Actions" align="center" min-width="220px">
        <template #default="scope">
          <el-button link type="primary" size="small"
                     @click="updateStu(scope.row.id); resetForm(stuFormRef)">
            Edit Profile
          </el-button>
          <el-button link type="warning" size="small" @click="openViolation(scope.row.id)">
            Update GPA
          </el-button>
          <el-button link type="danger" size="small" @click="delById(scope.row.id)">
            Drop
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <div style="margin-top: 20px; display: flex; justify-content: flex-end;">
      <el-pagination v-model:current-page="pagination.currentPage" v-model:page-size="pagination.pageSize"
                     :page-sizes="[10, 20, 50]" layout="total, sizes, prev, pager, next"
                     :total="pagination.total"
                     @size-change="handleSizeChange" @current-change="handleCurrentChange" />
    </div>

    <el-dialog v-model="dialogFormVisible" :title="formTitle" width="650px" top="5vh">
      <el-form :model="stu" ref="stuFormRef" :rules="rules" label-position="right">

        <div class="form-section-title">Basic Information</div>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="Full Name" :label-width="labelWidth" prop="name">
              <el-input v-model="stu.name" placeholder="Enter full legal name" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Student Number" :label-width="labelWidth" prop="no">
              <el-input v-model="stu.no" placeholder="9-digit York ID" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="Gender" :label-width="labelWidth" prop="gender">
              <el-select v-model="stu.gender" placeholder="Select" style="width: 100%;">
                <el-option v-for="gender in genders" :label="gender.name" :value="gender.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Phone" :label-width="labelWidth" prop="phone">
              <el-input v-model="stu.phone" placeholder="(416) 000-0000" />
            </el-form-item>
          </el-col>
        </el-row>

        <div class="form-section-title">Academic Details</div>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="Course" :label-width="labelWidth" prop="clazzId">
              <el-select v-model="stu.clazzId" placeholder="Select Course" style="width: 100%;">
                <el-option v-for="major in majors" :label="major.name" :value="major.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Year Level" :label-width="labelWidth" prop="yearLevel">
              <el-select v-model="stu.yearLevel" placeholder="Current Year" style="width: 100%;">
                <el-option v-for="level in yearLevels" :label="level.name" :value="level.value" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="Enrollment Status" :label-width="labelWidth" prop="enrollmentStatus">
              <el-select v-model="stu.enrollmentStatus" placeholder="Status" style="width: 100%;">
                <el-option v-for="status in enrollmentStatusOptions" :label="status.name" :value="status.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Exp. Grad Date" :label-width="labelWidth">
              <el-date-picker v-model="stu.graduationDate" type="date" placeholder="YYYY-MM-DD" value-format="YYYY-MM-DD" style="width: 100%;" />
            </el-form-item>
          </el-col>
        </el-row>

        <div class="form-section-title">Personal Records</div>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="SIN / Govt ID" :label-width="labelWidth" prop="idCard">
              <el-input v-model="stu.idCard" placeholder="Social Insurance / ID" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Address" :label-width="labelWidth">
              <el-input v-model="stu.address" placeholder="Current Residence" />
            </el-form-item>
          </el-col>
        </el-row>

      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogFormVisible = false">Cancel</el-button>
          <el-button type="primary" class="york-btn-red" @click="save(stuFormRef)">Save Record</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
/* York University Brand Colors */
:root {
  --york-red: #E31837;
  --york-dark: #333333;
}

.page-container {
  padding: 20px;
  background-color: #fff;
  border-radius: 8px;
}

#title {
  font-size: 26px;
  font-weight: 700;
  color: #333;
  margin-bottom: 25px;
  border-left: 5px solid #E31837; /* York Red Accent */
  padding-left: 15px;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif;
}

.form-section-title {
  font-size: 14px;
  color: #888;
  font-weight: 600;
  margin-bottom: 15px;
  margin-top: 10px;
  border-bottom: 1px solid #eee;
  padding-bottom: 5px;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

/* Custom York Red Button Style */
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

.toolbar {
  margin-bottom: 15px;
}
</style>