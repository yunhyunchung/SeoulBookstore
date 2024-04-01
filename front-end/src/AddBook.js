import React from 'react';
import { TextField, Paper, Button, Grid } from '@material-ui/core';
 
class AddBook extends React.Component {
    constructor(props) {
        super(props);
        this.state = { item: { title: "" } };   // state 객체에 사용자가 입력하는 book item title 값을 담을 준비
        this.add = props.add;   // App에게 전달받은 add 함수를 this.add에 연결 => add 호출 가능
    }

    // (1) 이벤트 핸들러 함수 작성
    onInputChange = (e) => {    // 사용자가 textfield에 key 입력을 할 때마다 이벤트 발생(핸들러)
        const thisItem = this.state.item;   // state 객체에 사용자가 입력하는 todo 아이템의 title 값을 담음
        thisItem.title = e.target.value;    // 이벤트에 담긴 value(book title)를 state 객체에 설정
        this.setState({ item: thisItem });  // 변경한 title 값 업데이트
        console.log(thisItem);
    }

    onButtonClick = () => {     // + 버튼을 클릭할 때마다 onInputChange에서 저장한 문자열을 리스트에 추가
        this.add(this.state.item);                  // add 함수 호출 => book 아이템 추가
        this.setState({ item: { title: "" } });     // 추가 후에는 현재 state 객체를 초기화함
    }

    enterKeyEventHandler = (e) => {     // Enter 키 누르면 todo item 추가 (onButtonClick() 재사용)
        if (e.key === 'Enter') {
            this.onButtonClick();
        }
    }

    render() {
        // (2) 이벤트 핸들러를 html 요소에 연결
        return (
            <Paper style={{ margin: 16, padding: 16 }}>     
                <Grid container>
                    <Grid xs={11} md={11} item style={{ paddingRight: 16 }}>    {/* ms, xs: 컬럼의 폭 */}
                        <TextField 
                            placeholder = "Add Todo here"
                            fullWidth
                            onChange={this.onInputChange}           // 사용자 key 입력이 일어날 때마다 실행되는 이벤트 핸들러
                            value={this.state.item.title}           // onInputChange 함수에서 변경한 title 값을 textfield value에 할당함  
                            onKeyPress={this.enterKeyEventHandler}  // 핸들러 연결 : enter 키 누르면 이벤트 발생
                        />
                    </Grid>
                    <Grid xs={1} md={1} item>
                        <Button
                            fullWidth
                            color="secondary"
                            variant="outlined"
                            onClick={this.onButtonClick}>    {/* + 버튼을 클릭하면, onButtonClick 함수가 호출됨, fullWidth: 컨테이너 전체를 차지하라 / variant: 버튼의 종류 ex) text, contained, outlined */}
                            +
                        </Button>
                    </Grid>
                </Grid>
            </Paper>
        );
    }
}

export default AddBook;