import React from 'react';
import { ListItem, ListItemText, InputBase, Checkbox, ListItemSecondaryAction, IconButton } from "@material-ui/core";
import { DeleteOutlined } from '@material-ui/icons';

class Book extends React.Component {
    constructor(props) { 
        super(props);
        this.state = { item: props.item, readOnly: true }   // App(부모)에게 물려받은 props의 item 속성에 담긴 객체를 state에 설정
        this.delete = props.delete;     // 부모로부터 delete, update 함수 전달받음     
        this.update = props.update;   
    }

    // Book item 삭제
    deleteEventHandler = () => {
        this.delete(this.state.item);   // delete 함수 호출하는 이벤트 핸들러
    }

    // title을 클릭하면 title 수정 가능한 이벤트 핸들러 (readOnly: false로 변경)
    offReadOnlyMode = () => {       
        console.log("Title Click Event!", this.state.readOnly);
        this.setState({ readOnly: false }, () => {
            console.log("ReadOnly?", this.state.readOnly);
        });
    }

    // title 수정 시 키 입력할 때마다 title을 변경하는 이벤트 핸들러
    editEventHandler = (e) => {
        const thisItem = this.state.item;
        thisItem.title = e.target.value;    // 이벤트 발생한 <InputBase>의 value 속성에 아이템의 title 값 있음
        this.setState({ item: thisItem });
    }

    // title 수정 후 Enter 키 누르면, 더 이상 수정 불가 (readOnly: true로 변경)한 이벤트 핸들러
    enterKeyEventHandler = (e) => {
        if (e.key === "Enter") {
            this.setState({ readOnly: true });
            this.update(this.state.item);   // 엔터 누르면 저장
        }
    }

    // 체크박스 체크, 해제하는 이벤트 핸들러
    checkboxEventHandler = (e) => {
        const thisItem = this.state.item;
        thisItem.done = !thisItem.done;     // 아이템의 done 값을 true <-> false로 전환
        this.setState({ item: thisItem });
        this.update(this.state.item);       // 체크박스가 변경되면 저장
    }

    render() {
        const item = this.state.item;   
        return (
            // Book: 부모(App)로부터 props 데이터를 전달받음. material ui 디자인에 checkbox, item title, 휴지통 아이콘을 그린다.
            <ListItem>
                <Checkbox checked={item.done}       // 체크박스 체크/해제
                        onChange={this.checkboxEventHandler} />   
                <ListItemText>
                    <InputBase
                        inputProps={{ 
                            "aria-label": "naked",
                            readOnly: this.state.readOnly,
                        }}                  // inputProps: <input>에 적용될 여러 가지 속성과 값들을 정의하는 속성
                        type="text"         // Textfield
                        id={item.id}        // props의 item에서 id 얻음
                        name={item.id}      // 아이템의 title이 동일해도 각 아이템을 구별하기 위해 item.id를 연결함
                        value={item.title}
                        multiline={true}
                        fullWidth={true}
                        onClick={this.offReadOnlyMode}          // title text 클릭하면 title 수정 가능
                        onChange={this.editEventHandler}        // 키 입력할 때마다 title 변경
                        onKeyPress={this.enterKeyEventHandler}  // Enter 키 누르면 title 수정 완료
                    />
                </ListItemText>
                <ListItemSecondaryAction>
                    <IconButton
                        aria-label="Delete Todo"
                        onClick={this.deleteEventHandler}>  
                        <DeleteOutlined />      {/* 삭제 아이콘 버튼에 삭제 이벤트 핸들러 추가 */}
                    </IconButton>
                </ListItemSecondaryAction>
            </ListItem>

            /*
            <div className="Book">
                <input 
                    type="checkbox"
                    id={this.state.item.id}
                    name={this.state.item.id}
                    checked={this.state.item.done} 
                />
                <label id={this.state.item.id}>{this.state.item.title}</label>
            </div>
            */
        );
    }
}

export default Book;

