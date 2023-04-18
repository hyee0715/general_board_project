## GENERAL BOARD PROJECT

> 게시판 기능을 하는 프로젝트 입니다.

## 프로젝트 소개
글을 작성하고 등록할 수 있는 기본적인 게시판 기능을 하는 프로젝트 입니다.

![image](https://user-images.githubusercontent.com/59169881/230759846-5c4bf4ab-49a3-482a-8d08-99d871c8f26b.png)


## Github 주소
https://github.com/hyee0715/general_board_project


## 프로젝트 주소
http://ec2-43-201-158-17.ap-northeast-2.compute.amazonaws.com:8080/


## 프로젝트 기능
  - 게시판
    - 게시물 목록 조회
    - 게시물 검색, 페이징
    - 게시물 상세 조회, 작성, 수정, 삭제, 게시물 조회수
    - 게시물 댓글 조회, 작성, 수정, 삭제

  - 회원
    - form 일반 회원 가입
      - 이메일 본인 인증
    - 소셜 회원 가입
      - 구글 계정, 네이버 계정
    - 로그인
      - 아이디 찾기, 비밀번호 찾기
      - 자동 로그인 기능
      
  - 설정
    - 회원 정보 수정
      - 닉네임, 비밀 번호, 프로필 사진
      - 작성한 게시물 목록 조회
      - 작성한 게시물 검색, 페이징
      - 작성한 게시물 선택 삭제
      - 회원 탈퇴
    - 프로필 사진 등록, 프로필 사진 다운로드


## 사용 기술
 - 백엔드
   - Framework & Library
     - Java 11
     - Spring Boot 2.7.5
     - Spring Security
     - OAuth 2.0
     - Spring Data JPA

   - Build Tool
     - Gradle 7.5.1

   - Database
     - MySQL 8.0.32

 - 프론트엔드
   - Framework & Library
     - Html / css
     - JavaScript / Ajax
     - Thymeleaf
     - Bootstrap



## 사용 예제

<details>
<summary>메인 페이지 / 회원 기능</summary>

![image](https://user-images.githubusercontent.com/59169881/230759846-5c4bf4ab-49a3-482a-8d08-99d871c8f26b.png)

해당 화면이 메인 페이지로, 게시글 전체 목록을 조회합니다.<br/>

게시글을 작성하기 위해 로그인을 해야합니다. 로그인을 하지 않은 상태에서 '글쓰기 버튼'을 누른다면 로그인 화면으로 강제 이동합니다.<br/>


![image](https://user-images.githubusercontent.com/59169881/230760141-9be5db5d-e293-4197-93ec-ed90fbf0830e.png)

로그인 페이지입니다. 소셜 로그인 또는 form을 통한 로그인을 할 수 있습니다.

![image](https://user-images.githubusercontent.com/59169881/230760306-7124a912-b122-441b-b7d6-bc401ceb3651.png)

유효하지 않은 값을 입력하면 오류 메시지를 출력합니다.

![image](https://user-images.githubusercontent.com/59169881/230770473-839bb6a9-e766-4762-b750-51fb6c5551ed.png)

구글 소셜 로그인 화면입니다.

![image](https://user-images.githubusercontent.com/59169881/230770489-b77d3640-a429-43fa-8faa-f7678906f55a.png)

네이버 소셜 로그인 화면입니다.


![image](https://user-images.githubusercontent.com/59169881/230760348-a79bb692-887b-491d-bb49-9231adbf478c.png)

회원가입 페이지 입니다.

![image](https://user-images.githubusercontent.com/59169881/232667037-5f2e61d3-d732-47a4-ac2b-2d1d5ae3066e.png)
유효하지 않은 값을 입력하면 오류 메시지를 표시합니다.


![image](https://user-images.githubusercontent.com/59169881/230769386-33ff34e3-21f1-448f-9b41-916b11a31521.png)

form 일반 회원가입 직후 본인 인증 이메일 안내 페이지 입니다. '이메일 인증하기' 버튼을 누르면 회원가입시에 입력했던 이메일로 본인 인증을 할 수 있습니다.

![image](https://user-images.githubusercontent.com/59169881/230769486-843e6607-f5b9-4736-8835-1a4d13007029.png)

![image](https://user-images.githubusercontent.com/59169881/230769492-ae97675d-8344-4793-93d8-b94ce31042df.png)

이메일 본인 인증을 마치면 새 창과 팝업창이 뜨며 메인 페이지로 redirect 됩니다.


![image](https://user-images.githubusercontent.com/59169881/230760377-fa5e1ac9-084c-4609-a928-d2f1dd768ff9.png)
아이디 찾기 페이지 입니다.

![image](https://user-images.githubusercontent.com/59169881/230768930-029d5c35-9ed7-4f2e-b83f-98b397e79e19.png)
유효하지 않은 값을 입력하면 오류 메시지를 표시합니다.


![image](https://user-images.githubusercontent.com/59169881/230768981-ef8e45b7-bbb8-4c00-8fa0-32862df3f623.png)
입력한 값과 일치하는 계정이 존재하면, 본인 인증 이메일을 전송합니다.

![image](https://user-images.githubusercontent.com/59169881/230769010-2e5fd8cb-d549-461f-bae7-cbfc7fbbfeb6.png)
![image](https://user-images.githubusercontent.com/59169881/230769735-f2f5e186-0558-4104-8b2c-2c7c2dc7ac78.png)

해당 이메일로 발송된 메일의 '메일 인증' 버튼을 누르면 새 창이 열리며 아이디를 확인할 수 있습니다.

![image](https://user-images.githubusercontent.com/59169881/230760386-82f6051e-a5cc-41bc-a945-efdf3a3757df.png)
비밀번호 찾기 페이지 입니다.

![image](https://user-images.githubusercontent.com/59169881/230769801-60569145-2c76-4f5c-828a-8eaaad01f283.png)
유효하지 않은 값을 입력하면 오류 메시지를 표시합니다.


![image](https://user-images.githubusercontent.com/59169881/230769833-9fcae920-44f0-4e8f-892e-b2b33492b842.png)
![image](https://user-images.githubusercontent.com/59169881/230769853-df3d2cf6-8623-437d-a6d5-040b18ffa3c3.png)

입력한 값과 일치하는 계정이 존재하면, 임시 비밀번호를 전송합니다. 임시 비밀번호로 로그인할 수 있습니다.
</details>

<details>
<summary>게시판 기능</summary>

![image](https://user-images.githubusercontent.com/59169881/230769979-e9e8e454-696c-468c-bd81-3f29a157cadc.png)

게시물 목록 하단의 번호를 클릭하면 다음 게시글들의 제목을 볼 수 있습니다.

![image](https://user-images.githubusercontent.com/59169881/230760482-e09159b7-41e1-46a2-8071-47b710c20217.png)
검색 옵션을 선택하여 원하는 키워드가 담긴 게시글을 조회할 수 있습니다.

![image](https://user-images.githubusercontent.com/59169881/230760704-3bb21546-0b05-4ccb-b407-947df92595f5.png)

메인 화면의 '글쓰기' 버튼을 누르면 게시글을 작성할 수 있습니다.

![image](https://user-images.githubusercontent.com/59169881/230760722-0441ab80-443f-4f03-9e5f-2aa679645aea.png)

게시글의 제목을 누르면 게시글의 상세 내용을 볼 수 있습니다.

자신이 작성한 게시물에는 수정, 삭제 버튼이 활성화 되어 수정, 삭제할 수 있습니다. 삭제버튼울 누르면 게시글 전체 목록으로 redirect 됩니다.

![image](https://user-images.githubusercontent.com/59169881/230770049-ff18ede8-cf2f-441a-a096-a8d6361a7af7.png)

자신이 작성한 게시물이 아닐 경우에는 수정, 삭제 버튼이 활성화되지 않습니다.


![image](https://user-images.githubusercontent.com/59169881/230760746-c7fdf03a-864a-4628-b302-46cb8825e58e.png)

수정 버튼을 눌러서 수정할 수 있습니다.

![image](https://user-images.githubusercontent.com/59169881/230770069-e053ff76-2361-431f-a3b5-75311724c189.png)

![image](https://user-images.githubusercontent.com/59169881/230770585-1f89ce47-2bef-486e-b028-64dac71f85ce.png)

로그인 후 댓글을 작성할 수 있습니다.


![image](https://user-images.githubusercontent.com/59169881/230770163-a060fedb-f24b-4deb-a2e3-f2a1b923a83d.png)

내가 작성한 댓글에만 수정, 삭제 버튼이 활성화 됩니다.

![image](https://user-images.githubusercontent.com/59169881/230770188-3cf6becf-4d6b-4de0-ba51-5be6dda89ecf.png)

![image](https://user-images.githubusercontent.com/59169881/230770209-e589b1e9-a91e-4ad3-a8f1-4ff9119760e2.png)

수정하기 버튼을 클릭하여 수정할 수 있습니다.

</details>


<details>
<summary>설정 기능</summary>

![image](https://user-images.githubusercontent.com/59169881/230760837-16093ff2-e280-4859-8c4b-9fd2b994785e.png)

마이페이지 버튼을 누르면 내 정보 수정 페이지로 이동합니다.

![image](https://user-images.githubusercontent.com/59169881/230760868-35c1d70c-8542-4359-bef0-396159c8f284.png)

닉네임과 프로필 사진을 변경할 수 있습니다.

![image](https://user-images.githubusercontent.com/59169881/230760883-bff80e0e-54f4-41f4-8f7f-efbd529b4188.png)
![image](https://user-images.githubusercontent.com/59169881/230760910-8d6d5596-0c50-4939-9147-377cdc34ea28.png)

해당 버튼을 눌러 프로필 사진 변경, 기존 이미지로 변경, 현재 프로필 사진 다운로드를 할 수 있습니다.

![image](https://user-images.githubusercontent.com/59169881/230770623-d3b2b8ec-e395-4f84-8149-bc27ab3a21cd.png)
![image](https://user-images.githubusercontent.com/59169881/230770631-bf66dd66-756b-4071-a9f0-784918be854d.png)

프로필 사진 등록 시 게시글과 댓글에서 프로필 사진을 확인할 수 있습니다.


![image](https://user-images.githubusercontent.com/59169881/230761026-a6a8c6ff-c765-4f52-aeff-0e390ad8400a.png)

왼쪽 내비게이션 바의 '비밀번호 변경'을 클릭하여 해당 페이지에서 비밀번호를 변경할 수 있습니다. 해당 기능은 form으로 가입한 회원에게만 활성화가 되며, 소셜 가입한 회원에게는 보이지 않습니다.

![image](https://user-images.githubusercontent.com/59169881/230760935-608ffb9e-66b4-41c0-adbe-4a59d27d18a2.png)

왼쪽 내비게이션 바의 '작성한 게시물'을 클릭하여 작성한 게시글을 모아볼 수 있습니다.

![image](https://user-images.githubusercontent.com/59169881/230760958-85439394-66d7-47dd-b89e-1fe6e178b742.png)

왼쪽 내비게이션 바의 '회원 탈퇴'을 클릭하여 회원을 탈퇴할 수 있습니다.

소셜 로그인 회원에게는 탈퇴 시에 이메일 입력을 요구합니다.

![image](https://user-images.githubusercontent.com/59169881/230770269-f2c895fc-3d60-43b6-9956-660914993344.png)

form 가입 회원에게는 탈퇴 시에 비밀번호 입력을 요구합니다.

![image](https://user-images.githubusercontent.com/59169881/230770343-b3369474-c999-4987-b4a4-06f81d08e19b.png)

탈퇴한 사용자의 게시물과 댓글은 그대로 남고, 댓글에는 댓글 작성자의 닉네임만 '탈퇴한 사용자'로 변경됩니다.
</details>

## 구조 및 설계

#### 1. 패키지 구조

<details>
<summary>패키지 구조 펼쳐보기</summary>


```
📦 src
 ┣ 📂 main
 ┃ ┣ 📂 java
 ┃ ┃ ┗ 📂 com
 ┃ ┃ ┃ ┗ 📂 hy
 ┃ ┃ ┃ ┃ ┗ 📂 general_board_project
 ┃ ┃ ┃ ┃ ┃ ┣ 📂 config
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂 auth
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂 dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 OAuthAttributes.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜 SessionUser.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 CustomAuthFailureHandler.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 CustomAuthSuccessHandler.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 PrincipalDetails.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜 PrincipalDetailsService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂 oauth
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜 PrincipalOauth2UserService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 Jpaconfig.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 SecurityConfig.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜 StorageConfig.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂 controller
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂 web
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 BoardController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 EmailController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 IndexController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 SettingController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 UserController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜 UserFindController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 BoardApiController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 CommentApiController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜 EmailApiController.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂 domain
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂 board
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 Board.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜 BoardRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂 comment
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 Comment.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜 CommentRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂 dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂 board
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 BoardDetailResponseDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 BoardListResponseDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 BoardSaveRequestDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 BoardSearchResponseDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜 BoardUpdateRequestDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂 comment
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 CommentRequestDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜 CommentResponseDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂 message
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜 MessageDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂 profileImage
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜 ProfileImageDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂 user
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 FindPasswordDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 FindUsernameDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 FormUserWithdrawRequestDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 SocialUserWithdrawRequestDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 UserDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 UserInfoUpdateRequestDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 UserPasswordUpdateRequestDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜 UserSignUpRequestDto.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂 profileImage
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 ProfileImage.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜 ProfileImageRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂 user
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 Role.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 User.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜 UserRepository.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜 Time.java
 ┃ ┃ ┃ ┃ ┃ ┃ 📂 Service
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 BoardService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 CommentService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 EmailService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 FileStoreService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 MessageService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 SettingService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 UserFindService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜 UserService.java
 ┃ ┃ ┃ ┃ ┃ ┣ 📂 validator
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂 validation
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📂 setting
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 CurrentPassword.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 CurrentPasswordValidationGroups.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 CurrentPasswordValidationSequence.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 CurrentPasswordValidator.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 NewPassword.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 NewPasswordConfirm.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 NewPasswordConfirmValidationGroups.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 NewPasswordConfirmValidationSequence.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 NewPasswordConfirmValidator.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 NewPasswordValidationGroups.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 NewPasswordValidationSequence.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 NewPasswordValidator.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 WithdrawEmail.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 WithdrawEmailValidationGroups.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 WithdrawEmailValidationSequence.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 WithdrawEmailValidator.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 WithdrawPassword.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 WithdrawPasswordValidationGroups.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 WithdrawPasswordValidationSequence.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜 WithdrawPasswordValidator.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📂 user
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 EmailValidationGroups.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 EmailValidationSequence.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 FindPassword.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 FindPasswordValidationGroups.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 FindPasswordValidationSequence.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 FindPasswordValidator.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 FindUsername.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 FindUsernameValidationGroups.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 FindUsernameValidationSequence.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 FindUsernameValidator.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 NicknameValidationGroups.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 NicknameValidationSequence.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 PasswordValidationGroups.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 PasswordValidationSequence.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 ReaNameValidationGroups.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 ReaNameValidationSequence.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 UsernameValidationGroups.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜 UsernameValidationSequence.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 AbstractValidator.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 CheckEmailAndProviderValidator.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 CheckNicknameModificationValidator.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 CheckNicknameValidator.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜 CheckUsernameValidator.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📜 GeneralBoardProjectApplication.java
 ┃ ┗ 📂 resources
 ┃ ┃ ┣ 📂 static
 ┃ ┃ ┃ ┣ 📂 css
 ┃ ┃ ┃ ┃ ┗ 📜 account.css
 ┃ ┃ ┃ ┃ ┗ 📜 bootstrap.min.css
 ┃ ┃ ┃ ┃ ┗ 📜 bootstrap.rtl.min.css
 ┃ ┃ ┃ ┃ ┗ 📜 dashboard.css
 ┃ ┃ ┃ ┃ ┗ 📜 dashboard.rtl.css
 ┃ ┃ ┃ ┃ ┗ 📜 headers.css
 ┃ ┃ ┃ ┣ 📂 image
 ┃ ┃ ┃ ┃ ┗ 📜defaultProfileImage.png
 ┃ ┃ ┃ ┗ 📂 js
 ┃ ┃ ┃ ┃ ┗ 📂 app
 ┃ ┃ ┃ ┃ ┃ ┗ 📜 bootstrap.bundle.min.js
 ┃ ┃ ┃ ┃ ┃ ┗ 📜 comment.js
 ┃ ┃ ┃ ┃ ┃ ┗ 📜 dashboard.js
 ┃ ┃ ┃ ┃ ┃ ┗ 📜 index.js
 ┃ ┃ ┣ 📂 templates
 ┃ ┃ ┃ ┣ 📂 account
 ┃ ┃ ┃ ┃ ┣ 📜 emailCertified.html
 ┃ ┃ ┃ ┃ ┣ 📜 findPassword.html
 ┃ ┃ ┃ ┃ ┣ 📜 findUsername.html
 ┃ ┃ ┃ ┃ ┣ 📜 findUsernameResult.html
 ┃ ┃ ┃ ┃ ┣ 📜 login.html
 ┃ ┃ ┃ ┃ ┗ 📜 signUp.html
 ┃ ┃ ┃ ┣ 📂 board
 ┃ ┃ ┃ ┃ ┣ 📜 detail.html
 ┃ ┃ ┃ ┃ ┣ 📜 search.html
 ┃ ┃ ┃ ┃ ┣ 📜 update.html
 ┃ ┃ ┃ ┃ ┗ 📜 write.html
 ┃ ┃ ┃ ┣ 📂 common
 ┃ ┃ ┃ ┃ ┗ 📜 messageRedirect.html
 ┃ ┃ ┃ ┣ 📂 fragments
 ┃ ┃ ┃ ┃ ┣ 📜 footer.html
 ┃ ┃ ┃ ┃ ┣ 📜 header.html
 ┃ ┃ ┃ ┃ ┗ 📜 settingFooter.html
 ┃ ┃ ┃ ┣ 📂 setting
 ┃ ┃ ┃ ┃ ┣ 📜 search.html
 ┃ ┃ ┃ ┃ ┣ 📜 userInfo.html
 ┃ ┃ ┃ ┃ ┣ 📜 userList.html
 ┃ ┃ ┃ ┃ ┣ 📜 userPassword.html
 ┃ ┃ ┃ ┃ ┗ 📜 withdrawal.html
 ┃ ┃ ┃ ┗ 📜 index.html
 ┃ ┃ ┣ 📜 application.yml
 ┃ ┃ ┣ 📜 application-aws.yml
 ┃ ┃ ┣ 📜 application-email.yml
 ┃ ┃ ┣ 📜 application-oauth.yml
 ┃ ┃ ┣ 📜 application-personal.yml
 ┃ ┃ ┗ 📜 error.properties
 ┗ 📂 test
 ┃ ┗ 📂 java
 ┃ ┃ ┗ 📂 com
 ┃ ┃ ┃ ┗ 📂 hy
 ┃ ┃ ┃ ┃ ┗ 📂 general_board_project
 ┃ ┃ ┃ ┃ ┃ ┣ 📂 service
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 BoardServiceTest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 CommentServiceTest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 SettingServiceTest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 UserFindServiceTest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜 UserServiceTest.java
 ┃ ┃ ┃ ┃ ┃ ┗ 📜 GeneralBoardProjectApplicationTests.java
```
</details>

#### 2. DB 설계

<img width="391" alt="db 설계" src="https://user-images.githubusercontent.com/59169881/230759302-8c8bad4e-bbdb-48ca-9060-03ca7d62d883.PNG"><br/>

<img width="445" alt="board entity" src="https://user-images.githubusercontent.com/59169881/230759320-3ebce833-04e6-4123-9bb7-8f8c4719870e.PNG"><br/>

<img width="527" alt="user entity" src="https://user-images.githubusercontent.com/59169881/230759325-909aef7f-57ea-42e7-9ef8-54a343447b80.PNG"><br/>

<img width="477" alt="profile image entity" src="https://user-images.githubusercontent.com/59169881/230759332-653c670d-19a9-48d2-8f15-cfcd2c9e323e.PNG"><br/>

<img width="447" alt="comment entity" src="https://user-images.githubusercontent.com/59169881/230759335-282832c5-07b0-422d-b5a2-c51cbbe708d9.PNG">



#### 3. API 설계

<img width="782" alt="게시글 관련 api" src="https://user-images.githubusercontent.com/59169881/230759429-7fe7f586-6d57-4814-acd0-82a5fc360543.PNG"><br/>

<img width="795" alt="회원 관련 api" src="https://user-images.githubusercontent.com/59169881/230759433-8ceae7c7-40c5-45ec-a87e-7da1aed35bbc.PNG"><br/>

<img width="886" alt="설정 관련 api" src="https://user-images.githubusercontent.com/59169881/230759435-570ff384-cfc9-4e9c-b5e1-0a4713d6327f.PNG"><br/>

<img width="518" alt="댓글 관련 ap i" src="https://user-images.githubusercontent.com/59169881/230759437-71c95b54-161c-4b9f-a34d-612e749616e8.PNG"><br/>


## 마치며
후에 리팩토링을 거치며 더욱 완성도 높은 프로젝트를 만들고 싶습니다. 부족한 점이 많아 공부하면서 업데이트 할 계획입니다.