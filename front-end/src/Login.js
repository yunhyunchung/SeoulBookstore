import React from "react";
import { signin } from "./service/ApiService";  // 로그인 요청 서비스
 
import { Link } from "@material-ui/core";
import Button from "@material-ui/core/Button";
import TextField from "@material-ui/core/TextField";
import Grid from "@material-ui/core/Grid";
import Typography from "@material-ui/core/Typography";
import { Container } from "@material-ui/core";

class Login extends React.Component {
    constructor(props) {
        super(props);
        this.handleSubmit = this.handleSubmit.bind(this);   // this가 가리키는 객체가 window 객체가 아니라 현재 <Login> 컴포넌트가 됨
    }

    handleSubmit(event) {
        event.preventDefault();     // ‘submit’ 버튼의 고유의 동작을 막아줌
        const data = new FormData(event.target);    // 이벤트 발생한 form 엘리먼트의 데이터를 담는 객체 (name:value = key:value)
        const email = data.get("email");
        const password = data.get("password");
        // ApiService의 signin 메서드 호출해서 로그인 요청 보냄
        signin({ email: email, password: password });
    }

    render() {
        return (
            <Container component="main" maxWidth="xs" style={{ marginTop: "8%" }}>
                <Grid container spacing={2}>
                    <Grid item xs={12}>
                        <Typography component="h1" variant="h5">
                            로그인
                        </Typography>
                    </Grid>
                </Grid>
                <form noValidate onSubmit={this.handleSubmit}>
                    {" "}
                    {/* submit 버튼을 클릭하면 handleSubmit 이벤트 핸들러가 실행됨 */}
                    <Grid container spacing={2}>
                        <Grid item xs={12}>
                            <TextField 
                                variant="outlined"
                                required
                                fullWidth
                                id="email"
                                label="이메일 주소"
                                name="email"
                                autoComplete="email"
                            />
                        </Grid>
                        <Grid item xs={12}>
                            <TextField 
                                variant="outlined"
                                required
                                fullWidth
                                name="password"
                                label="패스워드"
                                type="password"
                                id="password"
                                autoComplete="current-password"
                            />
                        </Grid>
                        <Grid item xs={12}>
                            <Button 
                                type="submit"
                                fullWidth
                                variant="contained"
                                color="primary"
                            >
                                로그인
                            </Button>
                        </Grid>
                        <Link href="/signup" variant="body2">
                            <Grid item>계정이 없습니까? 여기서 가입하세요.</Grid>
                        </Link>
                    </Grid>
                </form>
            </Container>
        );
    }
}

export default Login;

