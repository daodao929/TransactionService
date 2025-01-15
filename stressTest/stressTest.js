import http from 'k6/http';
import { check, sleep } from 'k6';

export let options = {
    vus: 100,
    duration: '30s',
};

export default function () {
    let response = http.get('http://localhost:8080/transactions');

    check(response, {
        'status is 200': (r) => r.status === 200,
        'response time is < 100ms': (r) => r.timings.duration < 100,
    });

    sleep(1);
}