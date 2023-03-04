<template>
  <div>
    <h1>회원가입</h1>
    <form @submit.prevent="submitForm">
      <div>
        <label for="username">이름:</label>
        <input type="text" id="username" v-model="username" required />
      </div>
      <div>
        <label for="email">이메일:</label>
        <input type="email" id="email" v-model="email" required />
      </div>
      <div>
        <label for="password">비밀번호:</label>
        <input type="password" id="password" v-model="password" required minlength="10" />
      </div>
      <div>
        <button type="submit">가입하기</button>
      </div>
    </form>
  </div>
</template>

<script>
export default {
  data () {
    return {
      username: '',
      email: '',
      password: ''
    }
  },
  methods: {
    async submitForm () {
      const url = 'http://localhost:8080/member-service/register'
      const data = {
        username: this.username,
        email: this.email,
        password: this.password
      }
      try {
        const response = await fetch(url, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(data)
        })
        if (!response.ok) {
          throw new Error('HTTP error ' + response.status)
        }
        alert('회원가입이 완료되었습니다.')
        this.$router.push('/login') // 회원가입 완료 후 로그인 페이지로 이동
      } catch (error) {
        console.error('Error:', error)
        alert('회원가입 중 오류가 발생하였습니다.')
      }
    }
  }
}
</script>
