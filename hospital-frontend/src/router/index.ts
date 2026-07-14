import { createRouter, createWebHistory } from 'vue-router'
import MainLayout from '@/layout/MainLayout.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      component: MainLayout,
      children: [
        { path: '', name: 'Home', component: () => import('@/views/HomeView.vue'), meta: { requiresAuth: false } },
        { path: 'doctors', name: 'Doctors', component: () => import('@/views/DoctorListView.vue'), meta: { requiresAuth: false } },
        { path: 'doctors/:did', name: 'DoctorDetail', component: () => import('@/views/DoctorDetailView.vue'), meta: { requiresAuth: false } },
        { path: 'my-appointments', name: 'MyAppointments', component: () => import('@/views/MyAppointmentsView.vue'), meta: { requiresAuth: true, role: 'patient' } },
        { path: 'messages', name: 'Messages', component: () => import('@/views/MessagesView.vue'), meta: { requiresAuth: true, role: 'patient' } },
        { path: 'patient-files', name: 'PatientFiles', component: () => import('@/views/PatientFilesView.vue'), meta: { requiresAuth: true, role: 'doctor' } },
        { path: 'schedule-manage', name: 'ScheduleManage', component: () => import('@/views/ScheduleManageView.vue'), meta: { requiresAuth: true, role: 'doctor' } },
        { path: 'admin/data', name: 'AdminData', component: () => import('@/views/AdminDataView.vue'), meta: { requiresAuth: true, role: 'doctor' } },
        { path: 'appointment/:did', name: 'Appointment', component: () => import('@/views/AppointmentView.vue'), meta: { requiresAuth: true, role: 'patient' } },
      ]
    },
    { path: '/login', name: 'Login', component: () => import('@/views/LoginView.vue'), meta: { requiresAuth: false } }
  ]
})

export default router
