import http from 'k6/http';
import { check, sleep } from 'k6';

const hostname = __ENV.SERVICE_HOSTNAME || 'localhost';
const baseUrl = `http://${hostname}:8080`;

export const options = {
  stages: [
    { duration: '30s', target: 20 },
    { duration: '30s', target: 20 }
  ],
  thresholds: {
    http_req_duration: ['p(95)<200'],
    http_req_failed: ['rate<0.01'],
  },
};

export default function () {
    let response = http.get(`${baseUrl}/transactions?page=0&size=5`);

    check(response, {
        'status is 200': (r) => r.status === 200,
        'response time is < 200ms': (r) => r.timings.duration < 200, 
    });

    sleep(1);
}