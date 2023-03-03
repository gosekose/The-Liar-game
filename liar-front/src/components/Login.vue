<template>
  <div>
    <h1>Login Page</h1>
    <form id="loginForm">
      <div>
        <label for="email">Email:</label>
        <input type="email" name="email" id="email" required>
      </div>
      <div>
        <label for="password">Password:</label>
        <input type="password" name="password" id="password" required>
      </div>
      </form>
    <button @click="submitForm">Login</button>
  </div>
</template>

<script>
import { login } from '@/login'

export default {
  methods: {
    submitForm () {
      const form = document.getElementById('loginForm')
      const formData = new FormData(form)

      login(formData)
        .then(response => {
          // 로그인 성공 처리
          localStorage.setItem('accessToken', response.data.accessToken)
          localStorage.setItem('refreshToken', response.data.refreshToken)
          localStorage.setItem('userId', response.data.userId)
        })
        .catch(error => {
          // 로그인 실패 처리
          console.log(error)
        })
    }
  }
}
</script>

<style>
/* 로그인 페이지 스타일링 */
</style>
