import React from "react";

// 테이블의 각 행을 나타내는 TodoRow 컴포넌트
class TodoRow extends React.Component {
    constructor(props) {
        super(props);
        this.state = { item: props.item } 
        this.add = props.add;  
        this.delete = props.delete;  
        this.update = props.update;
    }

    componentDidUpdate(prevProps) {
        // 이전 props와 현재 props를 비교하여 속성값이 변경된 경우에만 컴포넌트 상태 & UI 업데이트
        if (this.props.item !== prevProps.item) {
          this.setState({ item: this.props.item });
        }
    }

    render() {
        const item = this.state.item;
        return (
            <tr>
                <td>{item.id}</td>
                <td>{item.title}</td>
                <td>{item.author}</td>
                <td>{item.publisher}</td>
                <td>{item.userId}</td>
                <td>
                    <button onClick={this.delete}>delete</button>
                </td>
            </tr>
        );
    }
}

export default TodoRow;
