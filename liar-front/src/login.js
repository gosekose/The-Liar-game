import axios from 'axios'

const baseUrl = 'http://localhost:8000/member-service/login'

export function login (loginData) {
  return axios.post(`${baseUrl}/login`, loginData)
}
