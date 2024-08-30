<div align="center">
    <img width="100%" src="https://github.com/user-attachments/assets/dd04af34-9198-4a8a-bc60-4db3b97beb54"/>
</div>

<div align="center">
    
[🌐 웹페이지](http://updown.website/)    
    
</div>

<br>

# 🧳 프로젝트 소개

저희 비전은 영화 UP에서 영감을 받아, 고객들이 꿈꿔온 여행을 현실로 만들어 드리는 것입니다.  

영화 속 주인공들이 하늘 높이 날아오르며 꿈을 쫓는 것처럼, 저희는 고객들이 원하는 여행을 이뤄드리고자 합니다.

"**Up Down**" 프로젝트는 그 꿈을 담아 탄생했습니다. 이 프로젝트의 의미는 간단하지만 강력합니다. 
### 기분은 Up, 가격은 Down.
최고의 여행을 합리적인 가격에 제공하여, 고객들이 부담 없이 행복한 여행을 즐길 수 있도록 돕고자 합니다.

"**Up Down**" 프로젝트와 함께, 여러분의 여행은 기분 좋게 떠나고, 더 큰 만족을 안고 돌아오게 될 것입니다.

<br>

# 🔍 프로젝트 목표

> 최저가 여행 패키지 제공
* 최저가 국내 여행 패키지를 제공함으로써 상품의 가격을 비교하여 여행 비용을 최소화합니다.
* 패키지 여행의 상세 정보를 제공하여, 각 여행 상품의 특징을 명확히 파악하고, 비교할 수 있는 환경을 제공합니다.
* 이를 통해 사용자는 패키지 여행이 제공하는 편리함과 가성비를 활용하여 여행의 전반적인 만족도를 높입니다.

> 채팅방 기반의 커뮤니티 서비스
* 여행 패키지와 관련된 정보와 경험을 실시간으로 공유할 수 있는 채팅방 기반 커뮤니티 서비스를 제공합니다.
* 사용자들은 관심있는 테마에 따라 생성된 채팅방에 참여하여, 여행 패키지에 대한 질문과 후기를 공유할 수 있습니다.
  
> 사용자 맞춤형 
* 사용자가 선택한 테마와 관련된 최저가 패키지 여행을 추천합니다.
* 실시간 소통을 통해 패키지 여행의 정보를 빠르게 공유하고, 사용자들의 즉각적인 의견을 교환함으로써 최적의 선택을 할 수 있도록 돕습니다.

<br>

# 🔧 수행 도구

**programming** : ![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![JavaScript](https://img.shields.io/badge/javascript-%23323330.svg?style=for-the-badge&logo=javascript&logoColor=%23F7DF1E)
![HTML](https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white)
![CSS](https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white)

**framework** :
![Spring Boot](https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)

**DB** : 
![MySQL](https://img.shields.io/badge/mysql-4479A1.svg?style=for-the-badge&logo=mysql&logoColor=white)

**ORM** :
![Hibernate](https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=Hibernate&logoColor=white)

**협업툴** : 
![Slack](https://img.shields.io/badge/slack-%234A154B.svg?style=for-the-badge&logo=slack&logoColor=white)
![Discord](https://img.shields.io/badge/Discord-%235865F2.svg?style=for-the-badge&logo=discord&logoColor=white)
![Notion](https://img.shields.io/badge/Notion-%23000000.svg?style=for-the-badge&logo=notion&logoColor=white)

**디자인** :
![Figma](https://img.shields.io/badge/figma-%23F24E1E.svg?style=for-the-badge&logo=figma&logoColor=white)

**기타** : ![ElasticSearch](https://img.shields.io/badge/-ElasticSearch-005571?style=for-the-badge&logo=elasticsearch)
![Spring Batch](https://img.shields.io/badge/Spring%20Batch-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)
![GitHub Actions](https://img.shields.io/badge/github%20actions-%232671E5.svg?style=for-the-badge&logo=githubactions&logoColor=white)

<br>
 
# 🌵 브랜치 전략 

- Git-flow 전략을 기반으로 main, develop 브랜치와 feature 보조 브랜치를 운용했습니다.
- main, develop, feature 브랜치로 나누어 개발을 하였습니다.
    - **main** 브랜치는 배포 단계에서만 사용하는 브랜치입니다.
    - **develop** 브랜치는 개발 단계에서 git-flow의 master 역할을 하는 브랜치입니다.
    - **feature** 브랜치는 기능 단위로 독립적인 개발 환경을 위하여 사용하고 merge 후 각 브랜치를 삭제해주었습니다.

<br>

# 🚀 CI/CD

* github action을 활용해서 지속적인 통합 및 배포
* `develop`에서 `master`에서 Pull Request를 보내면, CI가 동작되고, Merge가 되면 운영 리소스에 배포된다.

<br>

# 🔨 프로젝트 구조

<br>

# 📂 디렉토리 구조

<pre>
UpProject/
│
├── 📂src/                  
│   ├── 📂main/
│   │   ├── 📂java/           
│   │   │   ├── 📂auth/
│   │   │   ├── 📂chatroom/
│   │   │   ├── 📂common/
│   │   │   ├── 📂config/
│   │   │   ├── 📂main/
│   │   │   ├── 📂product/    
│   │   │   ├── 📂search/
│   │   │   └── 📂user/
│   │   └── 📂resources/
│   │       ├── 📂static/
│   │       │   ├── 📂css/
│   │       │   ├── 📂js/
│   │       │   └── 📂scss/
│   │       └── 📂templates/
│   │           ├── 📂admin/
│   │           ├── 📂fragments/
│   │           ├── 📂join/
│   │           ├── 📂layout/
│   │           ├── 📂login/
│   │           └── 📂member/
│   └── 📂test/              
├── 📜Dockerfile                          
└── 📜README.md
</pre>

<br>

# ⭐ 주요기능

* **최저가 검색**
* **좋아요**
* **실시간 채팅**
* **로그인/회원가입**
#### [회원가입]
![회원가입gif](https://github.com/user-attachments/assets/0fb002fa-acf8-44d8-bb8b-81b964438d8b)

#### [소셜로그인_네이버]
![네이버 로그인-최종gif](https://github.com/user-attachments/assets/a7e9bfff-860b-4121-8ebd-7d2a255a5e03)

#### [소셜로그인_구글]
![구글 로그인](https://github.com/user-attachments/assets/f5a0a329-83d1-4bf0-9de3-382e9661baf1)

* **관리자**

<br>

# 💡 기대 효과

> 비용 절감 및 경제적 여행
* 사용자는 UpDown 플랫폼을 통해 최저가 국내 여행 패키지를 테마별로 검색하고 비교할 수 있어, 여행 비용을 효과적으로 절감할 수 있습니다.
> 사용자 만족도 향상
* UpDown 플랫폼의 포괄적인 정보 제공과 실시간 소통 기능은 사용자에게 더 나은 여행 선택을 지원합니다. 이는 전반적인 여행 만족도를 높이고, 사용자들의 재방문율을 증가시키며, 플랫폼의 신뢰성을 한층 강화하는데 기여합니다.
* 서로의 여행 경험과 팁을 공유함으로써 커뮤니티가 활성화되고, 플랫폼에 대한 신뢰성과 유용성이 증가합니다. 이는 사용자들 간의 정보 교류를 촉진하고, 최신 여행 정보를 지속적으로 업데이트하는데 기여합니다.

<br>

# 🎈 팀소개
## 👩‍💻 팀원 구성

<div align="center">

| **김연찬** | **오형동** | **박수빈** | **전현선** |
| :------: |  :------: | :------: | :------: |
| [<img src="https://avatars.githubusercontent.com/u/93385959?v=4" height=150 width=150> <br/> @hurryduck](https://github.com/hurryduck) | [<img src="https://avatars.githubusercontent.com/u/97084827?v=4" height=150 width=150> <br/> @dhgudehd98](https://github.com/dhgudehd98) | [<img src="https://avatars.githubusercontent.com/u/95676035?v=4" height=150 width=150> <br/> @subkka](https://github.com/subkka) | [<img src="https://avatars.githubusercontent.com/u/162237209?v=4" height=150 width=150> <br/> @jeonhyeonseon](https://github.com/jeonhyeonseon) |

</div>

## 💬 프로젝트 후기

> 김연찬

> 오형동

> 박수빈

> 전현선
