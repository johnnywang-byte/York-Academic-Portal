<script setup>
/**
 * York U Faculty & Department Directory
 * Management interface for Academic Units
 */
import { ref, onMounted } from "vue";
import { queryAllApi, addApi, queryByIdApi, updateApi, deleteApi } from "@/api/dept";
import { ElMessage, ElMessageBox } from "element-plus";

// Lifecycle hook
onMounted(() => {
  search();
});

// Fetch department list
const search = async () => {
  const result = await queryAllApi();
  deptList.value = result.data;
};

// Reactive state
const deptList = ref([]);
const dialogFormVisible = ref(false);
const dept = ref({ name: '' });
const formTitle = ref(''); // Fixed typo: fromTitle -> formTitle

// Form Reference
const deptFormRef = ref();

// Save Operation
const save = async () => {
  if (!deptFormRef.value) { return }

  deptFormRef.value.validate(async (valid) => {
    if (valid) {
      let result;
      if (dept.value.id) {
        result = await updateApi(dept.value);
      } else {
        result = await addApi(dept.value);
      }

      if (result.code) {
        ElMessage.success("Record saved successfully");
        dialogFormVisible.value = false;
        search();
      } else {
        ElMessage.error(result.msg);
      }
    } else {
      ElMessage.error("Validation failed. Please check inputs.");
    }
  });
};

// Open "Add" Dialog
const addDept = () => {
  dialogFormVisible.value = true;
  formTitle.value = 'New Academic Unit'; // Formal title
  dept.value = { name: '' };
  if (deptFormRef.value) {
    deptFormRef.value.resetFields();
  }
};

// Validation Rules
const rules = ref({
  name: [
    { required: true, message: "Faculty/Department name is required", trigger: "blur" },
    { min: 2, max: 50, message: "Length must be between 2 and 50 characters", trigger: "blur" },
  ],
});

// Open "Edit" Dialog
const edit = async (id) => {
  const result = await queryByIdApi(id);
  if (result.code) {
    dialogFormVisible.value = true;
    formTitle.value = 'Edit Unit Details';
    dept.value = result.data;
    if (deptFormRef.value) {
      deptFormRef.value.resetFields();
    }
  }
}

// Delete Operation
const delById = async (id) => {
  ElMessageBox.confirm(
    'This action will permanently remove this academic unit from the registry. Continue?',
    'Confirm Deletion',
    {
      confirmButtonText: 'Delete',
      cancelButtonText: 'Cancel',
      type: 'warning',
    }
  ).then(async () => {
    const result = await deleteApi(id);
    if (result.code) {
      ElMessage.success('Unit removed successfully');
      search();
    } else {
      ElMessage.error(result.msg);
    }
  }).catch(() => {
    // Cancelled
  });
}
</script>

<template>
  <div class="page-header">
    <div id="title">Faculties & Departments</div>
    <div class="subtitle">Academic Unit Configuration</div>
  </div>

  <div class="container toolbar">
    <el-button type="danger" class="york-btn-red" @click="addDept">+ New Academic Unit</el-button>
  </div>

  <div class="container">
    <el-table :data="deptList" border style="width: 100%"
              :header-cell-style="{background:'#f5f7fa', color:'#333', fontWeight:'700'}">
      <el-table-column type="index" label="#" width="80" align="center" />
      <el-table-column prop="name" label="Faculty / Department Name" min-width="300" align="left">
        <template #default="scope">
          <span style="font-weight: 600; color: #333;">{{ scope.row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="updateTime" label="Last Updated" width="220" align="center" />
      <el-table-column label="Actions" align="center" width="200">
        <template #default="scope">
          <el-button link type="primary" size="small" @click="edit(scope.row.id)">
            Edit
          </el-button>
          <el-button link type="danger" size="small" @click="delById(scope.row.id)">
            Remove
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>

  <el-dialog v-model="dialogFormVisible" :title="formTitle" width="500px">
    <el-form :model="dept" :rules="rules" ref="deptFormRef" label-position="top">
      <el-form-item label="Official Name" prop="name">
        <el-input v-model="dept.name" placeholder="e.g. Lassonde School of Engineering / Dept of EECS" />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogFormVisible = false">Cancel</el-button>
        <el-button type="primary" class="york-btn-red" @click="save">Save Changes</el-button>
      </div>
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

.toolbar {
  margin-bottom: 20px;
}

/* Custom Button Styles */
.york-btn-red {
  background-color: #E31837;
  border-color: #E31837;
}
.york-btn-red:hover {
  background-color: #C41230;
  border-color: #C41230;
}
</style>