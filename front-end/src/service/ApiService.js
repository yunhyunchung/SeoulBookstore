import { API_BASE_URL } from "../app-config";
const ACCESS_TOKEN = "ACCESS_TOKEN";

export function call(api, method, request) {    // 백엔드에 API 요청하는 call 함수 (Promise 객체 리턴)
    let headers = new Headers({
        "Content-Type": "application/json",
    });

    // 백엔드 요청 header에 token 추가
    const accessToken = localStorage.getItem("ACCESS_TOKEN");   // 1) 로컬 스토리지에서 ACCESS_TOKEN 가져오기
    if (accessToken && accessToken !== null) {
        headers.append("Authorization", "Bearer " + accessToken);   // 2) 백엔드 요청 시 header에 토큰 추가
    }
    
    let options = {             
        headers: headers,
        url: API_BASE_URL + api,
        method: method,
    };

    if (request) {      // 요청 시의 데이터가 있다면
        // GET method
        options.body = JSON.stringify(request);     // 요청 데이터를 json 문자열로 변환해서 보냄
    }

    // fetch() : 서버로 요청 보낸 후 Promise 객체 리턴
    return fetch(options.url, options)
        .then((response) =>    
            response.json().then((json) => {        // response.json()는 Promise 객체 반환
                if (!response.ok) {                 // response.ok가 false면 거부
                    return Promise.reject(json);    
                }
                return json;                        // true면 정상 응답을 json 객체 리턴
            })
        .catch((error) => {    
            console.log(error.status);
            if (error.status === 403) {             // (forbidden) 로그인이 되어 있지 않으면
                window.location.href = "/login";    // 로그인 페이지로 redirect
            }
            return Promise.reject(error);
        }
    ));
}

// 로그인 요청 (토큰 리턴함)
export function signin(userDTO) {
    return call("/auth/signin", "POST", userDTO).then((response) => {   // response = UserDTO 객체
        // console.log("response: ", response);
        // alert("로그인 토큰: " + response.token);    // 로그인 성공하면 토큰을 출력한 alert 창이 뜸

        if (response.token) {                                     // 로그인 응답에 token이 있으면 (로그인 성공)
            localStorage.setItem(ACCESS_TOKEN, response.token);   // 로컬 스토리지에 로그인 시 받은 토큰 저장
            window.location.href = "/";                           // Todo 화면으로 redirect 
        }
        // 개발자 도구 > Application에서 localStorage에 저장된 token 확인 가능
    });
}

// 계정 생성
export function signup(userDTO) {
    return call("/auth/signup", "POST", userDTO);
}

// 로그아웃
export function signout() {
    localStorage.setItem(ACCESS_TOKEN, null);   // 로컬 스토리지에 저장된 엑세스 토큰 제거
    window.location.href = "/login";            // 로그인 페이지로 redirect
}

