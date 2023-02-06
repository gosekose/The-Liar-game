<template>
  <div>
    <!-- your component template here -->
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      // your component data here
    };
  },
  methods: {
    async getDataFromAPI() {
      try {
        const token = 'your_token';
        const userId = 'your_userId';
        const requestBody = {
          body: 'koseRoomName',
          searchType: 'WAITROOMNAME',
          page: 0,
          limit: 10,
        };

        const headers = {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`,
          RefreshToken: `Bearer ${token}`,
          userId,
        };

        const response = await axios.post('your_api_endpoint', requestBody, { headers });

        if (response.status === 200) {
          const { code, message, body } = response.data;
          // do something with the response data
        }
      } catch (error) {
        console.error(error);
      }
    },
  },
};
</script>

