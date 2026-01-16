<script setup>
/**
 * York U Faculty & Staff Management
 * Refactored for Institutional Branding
 */
import { ref, watch, onMounted } from 'vue'
import { queryPageApi, addApi, queryInfoApi, updateApi, deleteApi } from '@/api/emp'
import { queryAllApi as queryAllDeptApi } from '@/api/dept'
import { ElMessage, ElMessageBox } from 'element-plus';

// -------- Metadata Definitions (York U Context) ----------

// Job Titles (Mapped to backend ID values)
// 职位名称修改为大学常用职称
const jobs = ref([
  { name: 'Course Director', value: 1 },      // 原 Class Instructor
  { name: 'Lecturer', value: 2 },             // 原 Lecturer
  { name: 'Student Success Officer', value: 3 }, // 原 Student Affairs
  { name: 'Research Lead', value: 4 },        // 原 Academic Supervisor
  { name: 'Academic Advisor', value: 5 },     // 原 Consultant
  { name: 'Administrative Staff', value: 6 }  // 原 Other
])

// Gender Options
const genders = ref([
  { name: 'Male', value: 1 },
  { name: 'Female', value: 2 }
])

// -------- Search Logic --------

// Search Parameters
const searchEmp = ref({ name: '', gender: '', date: [], begin: '', end: '' })

// Execute Search
const search = async () => {
  const result = await queryPageApi(
    searchEmp.value.name,
    searchEmp.value.gender,
    searchEmp.value.begin,
    searchEmp.value.end,
    currentPage.value,
    pageSize.value
  );

  if (result.code) {
    empList.value = result.data.rows;
    total.value = result.data.total
  }
}

// Clear Search Filters
const clear = () => {
  searchEmp.value = { name: '', gender: '', date: [], begin: '', end: '' };
  search();
}

// Lifecycle Hooks
onMounted(() => {
  search();         // Load employee list
  queryAllDepts();  // Load department options
  getToken();       // Retrieve auth token
})

// Watchers
watch(() => searchEmp.value.date, (newVal, oldVal) => {
  if (newVal && newVal.length == 2) {
    searchEmp.value.begin = newVal[0];
    searchEmp.value.end = newVal[1];
  } else {
    searchEmp.value.begin = '';
    searchEmp.value.end = '';
  }
})

// -------- Data Models --------

const empList = ref([]) // Table Data

// Pagination Configuration
const currentPage = ref(1)
const pageSize = ref(10)
const background = ref(true)
const total = ref(0)

const handleSizeChange = (val) => {
  search();
}

const handleCurrentChange = (val) => {
  search();
}

// -------- Form & Dialog Logic ----------

// Employee Form Model
const employee = ref({
  username: '',
  name: '',
  gender: '',
  phone: '',
  job: '',
  salary: '',
  deptId: '',
  entryDate: '',
  image: '',
  exprList: []
})

const dialogVisible = ref(false)
const dialogTitle = ref('New Faculty/Staff Appointment')

// File Upload Handlers
const handleAvatarSuccess = (response) => {
  employee.value.image = response.data;
}

const beforeAvatarUpload = (rawFile) => {
  if (rawFile.type !== 'image/jpeg' && rawFile.type !== 'image/png') {
    ElMessage.error('Format must be JPG or PNG.')
    return false
  } else if (rawFile.size / 1024 / 1024 > 10) {
    ElMessage.error('File size must be under 10MB.')
    return false
  }
  return true
}

// Initialize "Add Employee" Dialog
const addEmp = () => {
  dialogVisible.value = true;
  dialogTitle.value = 'New Faculty/Staff Appointment';
  // Reset model
  employee.value = {
    username: '',
    name: '',
    gender: '',
    phone: '',
    job: '',
    salary: '',
    deptId: '',
    entryDate: '',
    image: '',
    exprList: []
  }

  if (empFromRef.value) {
    empFromRef.value.resetFields();
  }
}

// Department Data
const depts = ref([]);
const queryAllDepts = async () => {
  const result = await queryAllDeptApi();
  if (result.code) {
    depts.value = result.data;
  }
}

// -------- Work Experience Logic -----------

const addExprItem = () => {
  employee.value.exprList.push({
    company: '', job: '', begin: '', end: '', exprDate: []
  });
}

const delExprItem = (index) => {
  employee.value.exprList.splice(index, 1);
}

watch(() => employee.value.exprList, (newVal, oldVal) => {
  if (employee.value.exprList && employee.value.exprList.length > 0) {
    employee.value.exprList.forEach((expr) => {
      if (expr.exprDate) {
        expr.begin = expr.exprDate[0];
        expr.end = expr.exprDate[1];
      }
    })
  }
}, { deep: true })


// ------- Persistence (Save/Update) --------

const empFromRef = ref();

const save = async () => {
  if (!empFromRef.value) return;

  await empFromRef.value.validate(async (valid) => {
    if (valid) {
      let result;
      if (employee.value.id) {
        result = await updateApi(employee.value);
      } else {
        result = await addApi(employee.value);
      }

      if (result.code) {
        ElMessage.success("Record Saved Successfully");
        dialogVisible.value = false;
        search();
      } else {
        ElMessage.error(result.msg);
      }
    } else {
      ElMessage.error('Please verify the form fields.');
    }
  })
}

// Validation Rules
const rules = ref({
  username: [
    { required: true, message: 'Passport York ID is required', trigger: 'blur' },
    { min: 2, max: 30, message: 'Length must be 2-30 chars', trigger: 'blur' }
  ],
  name: [
    { required: true, message: 'Full Legal Name is required', trigger: 'blur' },
    { min: 2, max: 30, message: 'Length must be 2-30 chars', trigger: 'blur' }
  ],
  gender: [
    { required: true, message: 'Selection required', trigger: 'change' }
  ],
  phone: [
    { required: true, message: 'Phone number is required', trigger: 'blur' },
    // North American Phone Format
    { pattern: /^(1?)[2-9]\d{9}$/, message: 'Invalid Format (e.g., 4165550199)', trigger: 'blur' }
  ]
});

// ------ Edit Logic -----
const edit = async (id) => {
  const result = await queryInfoApi(id);
  if (result.code) {
    dialogVisible.value = true;
    dialogTitle.value = 'Edit Faculty Profile';
    employee.value = result.data;

    let exprList = employee.value.exprList;
    if (exprList && exprList.length > 0) {
      exprList.forEach((expr) => {
        expr.exprDate = [expr.begin, expr.end];
      })
    }
  }
}

// ------ Delete Logic -----
const deleteById = (id) => {
  ElMessageBox.confirm(
    'This will permanently remove the personnel record. Continue?',
    'Confirm Deletion',
    {
      confirmButtonText: 'Delete',
      cancelButtonText: 'Cancel',
      type: 'warning',
    }
  ).then(async () => {
    const result = await deleteApi(id);
    if (result.code) {
      ElMessage.success('Record deleted');
      search();
    } else {
      ElMessage.error(result.msg);
    }
  }).catch(() => {});
}

// Bulk Delete Logic
const selectedIds = ref([]);

const handleSelectionChange = (selection) => {
  selectedIds.value = selection.map(item => item.id)
}

const deleteByIds = () => {
  ElMessageBox.confirm(
    'Delete all selected records?',
    'Batch Deletion',
    {
      confirmButtonText: 'Delete All',
      cancelButtonText: 'Cancel',
      type: 'warning',
    }
  ).then(async () => {
    if (selectedIds.value && selectedIds.value.length > 0) {
      const result = await deleteApi(selectedIds.value);
      if (result.code) {
        ElMessage.success('Batch deletion successful');
        search();
      } else {
        ElMessage.error(result.msg);
      }
    } else {
      ElMessage.error("No records selected");
    }
  }).catch(() => {});
}

// Token Management
const token = ref('');
const getToken = async () => {
  const loginUser = JSON.parse(localStorage.getItem('loginUser'))
  if (loginUser && loginUser.token) {
    token.value = loginUser.token;
  }
}
</script>

<template>
  <div class="page-header">
    <div id="title">Faculty & Staff Directory</div>
    <div class="subtitle">York University Human Resources</div>
  </div>

  <div class="container search-bar">
    <el-form :inline="true" :model="searchEmp" class="demo-form-inline">
      <el-form-item label="Name">
        <el-input v-model="searchEmp.name" placeholder="Search Faculty/Staff" />
      </el-form-item>
      <el-form-item label="Gender">
        <el-select v-model="searchEmp.gender" placeholder="All">
          <el-option label="Male" value="1" />
          <el-option label="Female" value="2" />
        </el-select>
      </el-form-item>
      <el-form-item label="Appointment Date">
        <el-date-picker v-model="searchEmp.date" type="daterange" range-separator="to" start-placeholder="From"
                        end-placeholder="To" value-format="YYYY-MM-DD" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" class="york-btn" @click="search">Search</el-button>
        <el-button class="york-btn-secondary" @click="clear">Reset</el-button>
      </el-form-item>
    </el-form>
  </div>

  <div class="container toolbar">
    <el-button type="danger" class="york-btn-red" @click="addEmp">+ New Appointment</el-button>
    <el-button plain type="danger" @click="deleteByIds">- Batch Remove</el-button>
  </div>

  <div class="container">
    <el-table :data="empList" border style="width: 100%" @selection-change="handleSelectionChange"
              :header-cell-style="{background:'#f5f7fa', color:'#333', fontWeight:'700'}">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column prop="name" label="Full Name" width="140" align="center">
        <template #default="scope">
          <span style="font-weight: 600; color: #E31837;">{{ scope.row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Gender" width="80" align="center">
        <template #default="scope">
          {{ scope.row.gender == 1 ? 'M' : 'F' }}
        </template>
      </el-table-column>
      <el-table-column label="Profile" width="100" align="center">
        <template #default="scope">
          <img :src="scope.row.image" height="40px" style="border-radius: 4px; border: 1px solid #eee;">
        </template>
      </el-table-column>
      <el-table-column prop="deptName" label="Faculty/Dept" width="140" align="center" />
      <el-table-column prop="job" label="Role / Title" width="160" align="center">
        <template #default="scope">
          <el-tag v-if="scope.row.job == 1" type="danger" effect="plain">Course Director</el-tag>
          <el-tag v-else-if="scope.row.job == 2" type="warning" effect="plain">Lecturer</el-tag>
          <el-tag v-else-if="scope.row.job == 3" type="success" effect="plain">Student Success</el-tag>
          <el-tag v-else-if="scope.row.job == 4" type="primary" effect="plain">Research Lead</el-tag>
          <el-tag v-else-if="scope.row.job == 5" type="info" effect="plain">Advisor</el-tag>
          <el-tag v-else effect="plain">Staff</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="entryDate" label="Appointed" width="120" align="center" />
      <el-table-column prop="updateTime" label="Last Updated" width="180" align="center" />
      <el-table-column label="Actions" align="center">
        <template #default="scope">
          <el-button link type="primary" size="small" @click="edit(scope.row.id)">
            Edit
          </el-button>
          <el-button link type="danger" size="small" @click="deleteById(scope.row.id)">
            Remove
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>

  <div class="container pagination-container">
    <el-pagination v-model:current-page="currentPage" v-model:page-size="pageSize"
                   :page-sizes="[5, 10, 20, 30, 50]" :background="background" layout="total, sizes, prev, pager, next, jumper"
                   :total="total" @size-change="handleSizeChange" @current-change="handleCurrentChange" />
  </div>

  <el-dialog v-model="dialogVisible" :title="dialogTitle" width="700px">
    <el-form :model="employee" label-width="120px" :rules="rules" ref="empFromRef">
      <div class="form-section-title">Personal Information</div>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="Passport York" prop="username">
            <el-input v-model="employee.username" placeholder="Username" ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="Full Name" prop="name">
            <el-input v-model="employee.name" placeholder="Legal Name"></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="Gender" prop="gender">
            <el-select v-model="employee.gender" placeholder="Select" style="width: 100%;">
              <el-option v-for="g in genders" :key="g.value" :label="g.name" :value="g.value"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="Phone" prop="phone">
            <el-input v-model="employee.phone" placeholder="(416) 000-0000"></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <div class="form-section-title">Employment Details</div>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="Role / Title">
            <el-select v-model="employee.job" placeholder="Select Title" style="width: 100%;">
              <el-option v-for="j in jobs" :key="j.value" :label="j.name" :value="j.value"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="Salary (CAD)">
            <el-input v-model="employee.salary" placeholder="Annual"></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="Faculty/Dept">
            <el-select v-model="employee.deptId" placeholder="Select Dept" style="width: 100%;">
              <el-option v-for="d in depts" :key="d.id"  :label="d.name" :value="d.id"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="Appointed On">
            <el-date-picker v-model="employee.entryDate" type="date" style="width: 100%;" placeholder="Select Date"
                            format="YYYY-MM-DD" value-format="YYYY-MM-DD"></el-date-picker>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="Profile Photo">
            <el-upload class="avatar-uploader" action="/api/upload" :show-file-list="false"
                       :on-success="handleAvatarSuccess" :before-upload="beforeAvatarUpload" :headers="{'token':token}">
              <img v-if="employee.image" :src="employee.image" class="avatar" />
              <el-icon v-else class="avatar-uploader-icon">
                <Plus />
              </el-icon>
            </el-upload>
          </el-form-item>
        </el-col>
      </el-row>

      <div class="form-section-title">Professional History</div>
      <el-row :gutter="10" >
        <el-col :span="24">
          <el-form-item label="Work History">
            <el-button type="success" size="small" plain @click="addExprItem">+ Add Position</el-button>
          </el-form-item>
        </el-col>
      </el-row>

      <div v-for="(expr,index) in employee.exprList" :key="index" class="expr-row">
        <el-row :gutter="5">
          <el-col :span="9">
            <el-date-picker type="daterange" v-model="expr.exprDate" range-separator="-" start-placeholder="Start" end-placeholder="End"
                            format="YYYY-MM-DD" value-format="YYYY-MM-DD" style="width: 100%" size="small"></el-date-picker>
          </el-col>
          <el-col :span="7">
            <el-input placeholder="Institution/Company" v-model="expr.company" size="small"></el-input>
          </el-col>
          <el-col :span="6">
            <el-input placeholder="Title" v-model="expr.job" size="small"></el-input>
          </el-col>
          <el-col :span="2">
            <el-button type="danger" circle size="small" @click="delExprItem(index)">
              <el-icon><Delete /></el-icon>
            </el-button>
          </el-col>
        </el-row>
      </div>
    </el-form>

    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">Cancel</el-button>
        <el-button type="primary" class="york-btn-red" @click="save">Save Record</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<style scoped>
/* York University Brand Colors */
:root {
  --york-red: #E31837;
}

#title {
  font-size: 26px;
  font-weight: 700;
  color: #333;
  margin-bottom: 5px;
  border-left: 5px solid #E31837; /* York Red Accent */
  padding-left: 15px;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif;
}

.subtitle {
  font-size: 14px;
  color: #666;
  margin-left: 20px;
  margin-bottom: 25px;
}

.container {
  margin: 15px 0px;
}

.form-section-title {
  font-size: 13px;
  color: #888;
  font-weight: 600;
  margin-bottom: 15px;
  margin-top: 10px;
  border-bottom: 1px solid #eee;
  padding-bottom: 5px;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

/* Button Styles */
.york-btn {
  background-color: #333;
  border-color: #333;
}
.york-btn:hover {
  background-color: #555;
  border-color: #555;
}

.york-btn-red {
  background-color: #E31837;
  border-color: #E31837;
}
.york-btn-red:hover {
  background-color: #C41230;
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

/* Avatar Styles */
.avatar {
  width: 90px;
  height: 90px;
  display: block;
  object-fit: cover;
  border-radius: 6px;
}

.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: #E31837;
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 90px;
  height: 90px;
  text-align: center;
  border-radius: 6px;
  border: 1px dashed var(--el-border-color);
  line-height: 90px;
}

.expr-row {
  margin-bottom: 8px;
  padding-bottom: 8px;
  border-bottom: 1px dashed #f0f0f0;
}

.pagination-container {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}
</style>