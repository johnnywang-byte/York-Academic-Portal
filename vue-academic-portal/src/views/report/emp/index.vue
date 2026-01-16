<script setup>
/**
 * Faculty & Staff Analytics (York U Style)
 * Visualization of Job Roles and Gender Distribution
 */
import { onMounted } from 'vue'
import * as echarts from 'echarts'
import { queryEmpJobDataApi, queryEmpGenderDataApi } from '@/api/report'

// Lifecycle hook
onMounted(() => {
  loadJobChart()    // Load Role Distribution
  loadGenderChart() // Load Gender Distribution
})

// 1. Fetch Job Role Statistics
// 获取职位统计 (后端现在直接返回英文，无需前端映射)
const loadJobChart = async () => {
  let result = await queryEmpJobDataApi();
  // Data comes from ReportServiceImpl -> JobOption
  let jobList = result.data.jobList;   // e.g. ['Lecturer', 'Class Master'...]
  let dataList = result.data.dataList; // e.g. [10, 5...]

  initJobChart(jobList, dataList)
}

// 2. Fetch Gender Statistics
// 获取性别统计 (后端直接返回 Male/Female)
const loadGenderChart = async () => {
  let result = await queryEmpGenderDataApi();
  let rawData = result.data; // e.g. [{name: 'Male', value: 10}, ...]

  initGenderChart(rawData)
}


/**
 * Chart 1: Staff Role Distribution (Bar Chart)
 * 职位分布柱状图 - York Red Theme
 */
function initJobChart(jobList, dataList) {
  var myChart = echarts.init(document.getElementById('container1'));

  myChart.setOption({
    title: {
      text: 'Staff Role Distribution',
      subText: 'Headcount by Position',
      textStyle: {
        fontSize: 18,
        fontWeight: 'bold',
        fontFamily: 'sans-serif'
      },
      left: 'center'
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '10%', // Extra space for rotated labels
      containLabel: true
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'shadow' }
    },
    xAxis: {
      type: 'category',
      data: jobList,
      axisLabel: {
        rotate: 30, // Rotate labels to avoid overlap
        interval: 0,
        fontWeight: 'bold',
        color: '#333'
      }
    },
    yAxis: {
      type: 'value',
      name: 'Staff Count'
    },
    series: [
      {
        name: 'Headcount',
        type: 'bar',
        barWidth: '45%',
        data: dataList,
        itemStyle: {
          // York University Red Gradient
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: '#E31837' }, // York Red
            { offset: 1, color: '#8B0000' }  // Darker Red
          ]),
          borderRadius: [4, 4, 0, 0],
          shadowColor: 'rgba(0, 0, 0, 0.2)',
          shadowBlur: 5
        }
      }
    ]
  });
}

/**
 * Chart 2: Gender Demographics (Pie Chart)
 * 性别分布饼图
 */
function initGenderChart(data) {
  var myChart = echarts.init(document.getElementById('container2'));

  let option = {
    title: {
      text: 'Gender Demographics',
      subText: 'Diversity Overview',
      textStyle: {
        fontSize: 18,
        fontWeight: 'bold',
        fontFamily: 'sans-serif'
      },
      left: 'center'
    },
    tooltip: {
      trigger: 'item',
      formatter: '{b}: {c} ({d}%)' // Name: Value (Percent%)
    },
    legend: {
      bottom: '5%',
      left: 'center',
      icon: 'circle'
    },
    // Professional Palette: Blue (Male), York Red (Female), Grey (Other)
    color: ['#003c71', '#E31837', '#A4A4A4'],
    series: [
      {
        name: 'Gender',
        type: 'pie',
        radius: ['45%', '70%'], // Donut style
        avoidLabelOverlap: false,
        center: ['50%', '50%'],
        itemStyle: {
          borderRadius: 5,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {
          show: false,
          position: 'center'
        },
        emphasis: {
          label: {
            show: true,
            fontSize: 24,
            fontWeight: 'bold',
            color: '#333'
          },
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        },
        data: data
      }
    ]
  };

  myChart.setOption(option);
}

</script>

<template>
  <div class="charts-wrapper">
    <div class="report_container" id="container1"></div>
    <div class="report_container" id="container2"></div>
  </div>
</template>

<style scoped>
.charts-wrapper {
  display: flex;
  justify-content: space-between; /* Place charts side by side */
  padding: 10px;
  background-color: #f5f7fa; /* Light grey background */
}

.report_container {
  width: 49%; /* Take up half width each */
  height: 80vh; /* Consistent height */
  background-color: #fff;
  border-radius: 8px;
  padding: 20px;
  box-sizing: border-box;
  /* Soft shadow for depth */
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  border: 1px solid #e0e0e0;
}

/* Responsive: Stack on small screens */
@media (max-width: 1200px) {
  .charts-wrapper {
    flex-direction: column;
  }
  .report_container {
    width: 100%;
    margin-bottom: 20px;
    height: 50vh;
  }
}
</style>