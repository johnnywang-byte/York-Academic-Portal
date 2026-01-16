<script setup>
/**
 * Student Analytics Component (York U Style)
 * Visualization of Course Sizes and Year Level Distribution
 */
import { onMounted } from 'vue'
import * as echarts from 'echarts'
import { queryStudentCountDataApi, queryStudentDegreeDataApi } from '@/api/report'

// Lifecycle hook - Load charts on mount
onMounted(() => {
  loadStudentCountChart() // Load Course Enrollment Chart
  loadDegreeChart()       // Load Year Level Chart
})

/**
 * CHART 1: Course Enrollment Statistics
 * Left Bar Chart
 */
const loadStudentCountChart = async () => {
  let result = await queryStudentCountDataApi();
  // Backend returns: clazzList (X-axis names), dataList (Y-axis values)
  let clazzList = result.data.clazzList;
  let dataList = result.data.dataList;

  initStudentCountChart(clazzList, dataList)
}

function initStudentCountChart(clazzList, dataList) {
  var myChart = echarts.init(document.getElementById('container1'));

  myChart.setOption({
    title: {
      text: 'Course Enrollment',
      subText: 'Total Students per Course',
      textStyle: {
        fontSize: 20,
        fontWeight: 'bold',
        fontFamily: 'sans-serif'
      },
      left: 'center'
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '10%', // Increased bottom space for rotated labels
      containLabel: true
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'shadow' }
    },
    xAxis: {
      type: 'category',
      data: clazzList,
      axisLabel: {
        rotate: 30, // Rotate labels for long course codes
        interval: 0,
        color: '#333',
        fontWeight: 'bold'
      }
    },
    yAxis: {
      type: 'value',
      name: 'Students'
    },
    series: [
      {
        name: 'Enrolled',
        type: 'bar',
        barWidth: '40%',
        data: dataList,
        itemStyle: {
          // York University Red Gradient
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: '#E31837' }, // York Red
            { offset: 1, color: '#8B0000' }  // Dark Red
          ]),
          borderRadius: [4, 4, 0, 0]
        }
      }
    ]
  });
}

/**
 * CHART 2: Student Year Level Distribution
 * Right Pie Chart
 */
const loadDegreeChart = async () => {
  let result = await queryStudentDegreeDataApi();
  // Backend now returns English names ('1st Year', '2nd Year'...), so no mapping needed!
  let chartData = result.data;

  initDegreeChart(chartData)
}

function initDegreeChart(degreeDataList) {
  var myChart = echarts.init(document.getElementById('container2'));

  let option = {
    title: {
      text: 'Student Demographics',
      subText: 'Distribution by Year Level',
      textStyle: {
        fontSize: 20,
        fontWeight: 'bold',
        fontFamily: 'sans-serif'
      },
      left: 'center'
    },
    tooltip: {
      trigger: 'item',
      formatter: '<b>{b}</b><br/>{c} Students ({d}%)'
    },
    legend: {
      bottom: '5%',
      left: 'center',
      icon: 'circle'
    },
    // York U Corporate Color Palette (Red, Grey, Black, White accents)
    color: [
      '#E31837', // York Red
      '#333333', // Black
      '#A4A4A4', // Silver/Grey
      '#D6D6D6', // Light Grey
      '#8B0000', // Dark Red
      '#000000'  // Pure Black
    ],
    series: [
      {
        name: 'Year Level',
        type: 'pie',
        radius: ['45%', '70%'], // Donut chart style
        center: ['50%', '50%'],
        avoidLabelOverlap: false,
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
            color: '#E31837' // Red text on hover
          },
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        },
        labelLine: {
          show: false
        },
        data: degreeDataList
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
  justify-content: space-between;
  padding: 10px;
  background-color: #f5f7fa; /* Light grey background for the whole page */
  height: 100%;
}

.report_container {
  width: 49%;
  height: 80vh;
  background-color: #fff;
  border-radius: 8px;
  padding: 20px;
  box-sizing: border-box;
  /* Soft shadow for depth */
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  border: 1px solid #e0e0e0; /* Subtle border */
}

/* Responsive adjustment for smaller screens */
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