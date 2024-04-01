// 라우팅 규칙을 가지는 컴포넌트
// index.js에서 이 컴포넌트가 제일 먼저 렌더링되도록 함

import React from "react";
import "./index.css";

import App from "./App";
import Login from "./Login";
import SignUp from "./SignUp";

// 리액트 라우터로 BrowserRouter와 HashRouter가 많이 쓰임
import { BrowserRouter as Router, Switch, Route } from "react-router-dom";

import Box from "@material-ui/core/Box";
import Typography from "@material-ui/core/Typography";

// footer 부분
function Copyright() {
    return (
        <Typography variant="body2" color="textSecondary" align="center">
            {"Copyright © "}
            fsoftwareengineer, {new Date().getFullYear()}
            {"."}
        </Typography>
    )
}

class AppRouter extends React.Component { 
    render() {
        return (
            <div>
                <Router>
                    <div>
                        <Switch>    { /* 해당 URL에 따라 다른 컴포넌트를 보여주는 <Route> */ }
                            <Route path="/login">   
                                <Login />   {/* “localhost:3000/login” 요청이 오면,<Login> 컴포넌트를 렌더링해라! */}
                            </Route>
                            <Route path="/signup">
                                <SignUp />  
                            </Route>
                            <Route path="/">
                                <App />     {/* “localhost:3000/” 요청이 오면, <App> 컴포넌트를 렌더링해라! */}
                            </Route>
                        </Switch>
                    </div>
                    <Box mt={5}>
                        <Copyright />
                    </Box>
                </Router>
            </div>
        )
    }
}

export default AppRouter;



