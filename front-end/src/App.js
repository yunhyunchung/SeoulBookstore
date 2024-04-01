import React from 'react';
import { Container, Button } from "@material-ui/core";
import { Grid, AppBar, Toolbar, Tabs, Tab, Typography } from '@material-ui/core';
import { TextField } from '@material-ui/core';
import './App.css';
import TodoRow from './TodoRow';
import { call, signout } from './service/ApiService';    // 백엔드 서비스 요청, 로그아웃 추가

import AddIcon from '@material-ui/icons/Add';   // Tab에 들어갈 아이콘들
import SearchIcon from '@material-ui/icons/Search';
import EditIcon from '@material-ui/icons/Edit';
import DeleteIcon from '@material-ui/icons/Delete';

class App extends React.Component {
  constructor(props) {  
    super(props);       // 부모 생성자 반드시 호출
    this.state = {      
      items : [ ],          // Book item list는 App 컴포넌트가 관리
      item: { title: '', author: '', publisher: '', userId: '' },    // 사용자가 입력하는 값을 담는 book item
      activeMenu: 'add',    // 현재 선택된 Menu 항목을 나타내는 상태값
      loading: true,        // '로딩 중'이라는 상태 표시
    };
  }

  handleMenuChange = (e, newMenu) => {    // 메뉴 클릭 시 동작하는 이벤트 핸들러
    this.setState({ activeMenu: newMenu });
  }

  componentDidMount() {     // 백엔드 api를 호출해 todo item list를 얻어온다.
    call("/book", "GET", null).then((response) => {
      this.setState({ items: response.data , loading: false});    // GET 요청으로 Todo 리스트 가져오기 성공 => 로딩 완료 (로딩 중 false)
    });
  }

  add = (item) => {
    call("/book", "POST", item).then((response) => {
      this.setState({ items: response.data, item: { title: '', author: '', publisher: '', userId: '' } });
    });
  }

  retrieve = (item) => {
    call(`/book?title=${item.title}`, "GET", null).then((response) => {
      const retrievedItem = response.data[0];
      this.setState({
        items: [retrievedItem],
        item: {
          title: retrievedItem.title,
          author: retrievedItem.author,
          publisher: retrievedItem.publisher,
          userId: retrievedItem.userId,
        },
      });
    });
  }

  update = () => {
    const { item, items } = this.state;
    const selectedItemId = items[0].id;    // 현재 검색된 제품의 id를 가져옴
    const updatedItem = {       // 수정할 제품 정보를 생성
      id: selectedItemId,
      title: item.title,
      author: item.author,
      publisher: item.publisher,
      userId: item.userId
    };

    // 백엔드 서버로 수정된 제품 정보 전송 & DB에 업데이트
    call(`/book`, "PUT", updatedItem)
      .then((response) => {
        const updatedItems = items.map((item) =>
          item.id === selectedItemId ? response.data[0] : item
        );
        this.setState({
          items: updatedItems,
          item: { title: '', author: '', publisher: '', userId: '' },
        });
      })
      .catch((error) => {
        console.error("Error updating item:", error);
      });
  };

  delete = (item) => {    // book item table의 삭제 버튼과 연결
    call("/book", "DELETE", item).then((response) => {
      this.setState({ 
        items: response.data, 
        item: { title: '', author: '', publisher: '', userId: '' } });
    });
  }

  deleteProduct = () => {   // /book/{title} 경로로 제품 삭제하기
    const { item } = this.state;
    call(`/book/${item.title}`, "DELETE", null)
      .then((response) => {
        const { data, error } = response;
        if (error) {
          console.log(error); // 오류가 발생한 경우 콘솔에 오류 메시지 출력
        } else {
          this.setState({
            items: data,
            item: { title: '', author: '', publisher: '', userId: '' },
          });
        }
      })
      .catch((error) => {
        console.log(error); 
      });
  };  

  handleInputChange = (e) => {
    const { name, value } = e.target;
    this.setState((prevState) => ({
      item: { ...prevState.item, [name]: value },
    }));
  };

  render() {
    const { items, item, activeMenu } = this.state;

    // 상단 내비게이션 바 (with 로그아웃 버튼)
    var navigationBar = (
      <AppBar position="static">
        <Toolbar>
          <Grid justify="space-between" container>
            <Grid item>
              <Typography variant="h6" style={{ fontWeight: 800, fontSize: 22 }}>서울 책방</Typography>
            </Grid>
            <Grid>
              <Button color="inherit" onClick={signout}>
                로그아웃
              </Button>
            </Grid>
          </Grid>
        </Toolbar>
      </AppBar>
    );

    // 제품 DB 테이블
    var todoRows = items.map((item) => (
      <TodoRow
        item={item}
        key={item.id}
        delete={() => this.delete(item)}
        update={this.update}
      />
    ));

    // 제품 추가 메뉴
    const addMenu = (
      <div>
        <Typography variant="h6" style={{ fontWeight: 600, marginBottom: 10 }}>
          새로운 책 추가하기
        </Typography>
        <table className='addBook'>
          <tbody>
            {/* <caption>제품 추가하기</caption> */}
            <tr>
              <td>
                <label htmlFor="title" style={{ fontWeight: '500' }}>Title</label>
              </td>
              <td>
                <TextField
                  id="title"
                  label="제목"
                  variant='outlined'
                  margin="dense"
                  type="text"
                  name="title"
                  value={item.title}
                  onChange={this.handleInputChange}
                />
              </td>
              <td width="50"></td>
              <td></td>
            </tr>
            <tr>
              <td>
                <label htmlFor="author" style={{ fontWeight: '500' }}>Author</label>
              </td>
              <td>
                <TextField
                  id="author"
                  label="저자"
                  variant='outlined'
                  type="text"
                  margin="dense"
                  name="author"
                  value={item.author}
                  onChange={this.handleInputChange}
                />
              </td>
              <td></td>
              <td></td>
            </tr>
            <tr>
              <td>
                <label htmlFor="publisher" style={{ fontWeight: '500' }}>Publisher</label>
              </td>
              <td>
                <TextField
                  id="publisher"
                  label="출판사"
                  variant='outlined'
                  type="text"
                  margin="dense"
                  name="publisher"
                  value={item.publisher}
                  onChange={this.handleInputChange}
                />
              </td>
              <td></td>
              <td></td>
            </tr>
            <tr>
              <td>
                <label htmlFor="userId" style={{ fontWeight: '500' }}>UserId</label>
              </td> 
              <td>
                <TextField
                  id="userId"
                  label="사용자 ID"
                  variant='outlined'
                  type="text"
                  margin="dense"
                  name="userId"
                  value={item.userId}
                  onChange={this.handleInputChange}
                />
              </td>
              <td></td>
              <td>
                <Button
                  color="primary"
                  variant="contained"
                  onClick={() => this.add(item)}
                >
                  제품 추가
                </Button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    );

    // 제품 검색 메뉴
    const searchMenu = (
      <div>
        <Typography variant="h6" style={{ fontWeight: 600, marginBottom: 10 }}>
          궁금한 책 검색하기
        </Typography>
        <table className='retrieveBook'>
            {/* <caption>제품 검색하기</caption> */}
            <tr>
              <td>
                <label htmlFor="title" style={{ fontWeight: '500' }}>Title</label>
              </td>
              <td>
                <TextField
                  id="title"
                  label="제목"
                  variant='outlined'
                  margin="dense"
                  type="text"
                  name="title"
                  value={item.title}
                  onChange={this.handleInputChange}
                />
              </td>
              <td width="50"></td>
              <td>
                <Button
                  color="primary"
                  variant="contained"
                  onClick={() => this.retrieve(item)}
                >
                  제품 검색
                </Button>
              </td>
            </tr>
            <tr>
              <td>
                <label htmlFor="author" style={{ fontWeight: '500' }}>Author</label>
              </td>
              <td>
                <TextField
                  id="author"
                  label="저자"
                  variant='outlined'
                  margin="dense"
                  type="text"
                  name="author"
                  value={item.author}
                  onChange={this.handleInputChange}
                />
              </td>
              <td></td>
              <td></td>
            </tr>
            <tr>
              <td>
                <label htmlFor="publisher" style={{ fontWeight: '500' }}>Publisher</label>
              </td>
              <td>
                <TextField
                  id="publisher"
                  label="출판사"
                  variant='outlined'
                  margin="dense"
                  type="text"
                  name="publisher"
                  value={item.publisher}
                  onChange={this.handleInputChange}
                />
              </td>
              <td></td>
              <td></td>
            </tr>
            <tr>
              <td>
                <label htmlFor="userId" style={{ fontWeight: '500' }}>UserId</label>
              </td> 
              <td>
                <TextField
                  id="userId"
                  label="사용자 ID"
                  variant='outlined'
                  margin="dense"
                  type="text"
                  name="userId"
                  value={item.userId}
                  onChange={this.handleInputChange}
                />
              </td>
              <td></td>
              <td></td>
            </tr>
          </table>
      </div>
    );

    // 제품 수정 메뉴
    const updateMenu = (
      <div>
        <Typography variant="h6" style={{ fontWeight: 600, marginBottom: 10 }}>
          검색한 책 수정하기
        </Typography>
        <table className='update'>
            {/* <caption>제품 수정하기</caption> */}
            <tr>
              <td>
                <label htmlFor="title" style={{ fontWeight: '500' }}>Title</label>
              </td>
              <td>
                <TextField
                  id="title"
                  label="제목"
                  variant='outlined'
                  margin="dense"
                  type="text"
                  name="title"
                  value={item.title}
                  onChange={this.handleInputChange}
                />
              </td>
              <td width="50"></td>
              <td>
                <Button
                  color="primary"
                  variant="contained"
                  onClick={() => this.retrieve(item)}
                >
                  제품 검색
                </Button>
              </td>
            </tr>
            <tr>
              <td>
                <label htmlFor="author" style={{ fontWeight: '500' }}>Author</label>
              </td>
              <td>
                <TextField
                  id="author"
                  label="저자"
                  variant='outlined'
                  margin="dense"
                  type="text"
                  name="author"
                  value={item.author}
                  onChange={this.handleInputChange}
                />
              </td>
              <td></td>
              <td></td>
            </tr>
            <tr>
              <td>
                <label htmlFor="publisher" style={{ fontWeight: '500' }}>Publisher</label>
              </td>
              <td>
                <TextField
                  id="publisher"
                  label="출판사"
                  variant='outlined'
                  margin="dense"
                  type="text"
                  name="publisher"
                  value={item.publisher}
                  onChange={this.handleInputChange}
                />
              </td>
              <td></td>
              <td></td>
            </tr>
            <tr>
              <td>
                <label htmlFor="userId" style={{ fontWeight: '500' }}>UserId</label>
              </td> 
              <td>
                <TextField
                  id="userId"
                  label="사용자 ID"
                  variant='outlined'
                  margin="dense"
                  type="text"
                  name="userId"
                  value={item.userId}
                  onChange={this.handleInputChange}
                />
              </td>
              <td></td>
              <td>
                <Button
                  color="primary"
                  variant="contained"
                  onClick={this.update}   
                >
                  제품 수정
                </Button>
              </td>
            </tr>
          </table>
      </div>
    );

    // 제품 삭제 메뉴
    const deleteMenu = (
      <div>
        <Typography variant="h6" style={{ fontWeight: 600, marginBottom: 10 }}>
          기존의 책 삭제하기
        </Typography>
        <table className='delete'>
            {/* <caption>제품 삭제하기</caption> */}
            <tr>
              <td>
                <label htmlFor="title" style={{ fontWeight: '500' }}>Title</label>
              </td>
              <td>
                <TextField
                  id="title"
                  type="text"
                  label="제목"
                  variant='outlined'
                  margin="dense"
                  name="title"
                  value={item.title}
                  onChange={this.handleInputChange}
                />
              </td>
              <td width="50"></td>
              <td>
                <Button
                  color="primary"
                  variant="contained"
                  onClick={this.deleteProduct}
                >
                  제품 삭제
                </Button>
              </td>
            </tr>
          </table>
      </div>
    );

    let content;    // 메뉴 값 content
    switch (activeMenu) {
      case 'add':
        content = addMenu;
        break;
      case 'search':
        content = searchMenu;
        break;
      case 'update':
        content = updateMenu;
        break;
      case 'delete':
        content = deleteMenu;
        break;
      default:
        content = null;
        break;
    }

    // 로딩 완료했을 때 표시할(렌더링할) 내용 (로딩 중 아님)
    var bookListPage = (
      <div className="App">

        {/* 상단 내비게이션 바 (with 로그아웃) */}
        {navigationBar}

        {/* 제품 CRUD 기능 UI, 4개의 Tab으로 표현 */}
        <div className="TabContainer">
          <Tabs value={activeMenu} onChange={this.handleMenuChange} textColor="primary" indicatorColor="primary" centered>
            <Tab icon={<AddIcon />} label="추가" value="add" />
            <Tab icon={<SearchIcon />} label="검색" value="search" />
            <Tab icon={<EditIcon />} label="수정" value="update" />
            <Tab icon={<DeleteIcon />} label="삭제" value="delete" />
          </Tabs>
          {content}
        </div>

        {/* 제품 DB table */}
        <Container maxWidth="md">
          <div className="BookTable">
            <table border="1">
              <caption>Book item table</caption>
              <thead>
                <tr>
                  <th>id</th>
                  <th>title</th>
                  <th>author</th>
                  <th>publisher</th>
                  <th>userId</th>
                  <th>삭제 버튼</th>
                </tr>
              </thead>
              <tbody>{todoRows}</tbody>
            </table>
          </div>
        </Container>

      </div>
    )

    // 로딩 중일 때 표시할 내용
    var loadingPage = <h1> 로딩 중... </h1>;
    var loadingContent = loadingPage;         // 로딩 중 표시를 변수에 할당함

    if (!this.state.loading) {    /* 로딩 중 false면 bookListPage 렌더링 */
      loadingContent = bookListPage;
    }

    return (
      <div className="App">
        {loadingContent}        
      </div>
    );
  }
}

export default App;
