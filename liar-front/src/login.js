import axios from 'axios'

const baseUrl = 'http://localhost:8080'

export function login (loginData) {
  return axios.post(`${baseUrl}/login`, loginData)
}
