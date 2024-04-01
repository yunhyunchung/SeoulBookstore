/* 백엔드 서비스 주소를 설정하기 위한 파일 */
 
let backendHost;

// (1) 현재 브라우저의 domain name을 얻음
const hostname = window && window.location && window.location.hostname;

if (hostname === "localhost") {   // (2) 현재 브라우저의 domain name이 “localhost” 이면...
    backendHost = "http://localhost:8080";  // (3) 이 주소를 백엔드 서비스 주소로 설정함
}

export const API_BASE_URL = `${backendHost}`;   // (4) 이 주소를 API_BASE_URL이름의 변수에 담아 export함