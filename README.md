<div align="center">
    <img width="100%" src="https://github.com/user-attachments/assets/dd04af34-9198-4a8a-bc60-4db3b97beb54"/>
</div>

<div align="center">
    
[🌐 웹페이지](http://updown.website/)   
[🌐 up-data-server](https://github.com/dhgudehd98/up-data-server)
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

## **최저가 검색 서비스**
* **최저가 검색**
  
![최저가 검색](https://github.com/user-attachments/assets/b8d0a62d-e65a-478d-ad5f-c4b31e0be451)

* **테마별 최저가 검색**
  
![최저가 테마별 (1)](https://github.com/user-attachments/assets/78f763f0-9305-4d69-aca0-caef5dd6ad67)

* **좋아요**

## **커뮤니티 서비스**
* **채팅방 생성**

* **실시간 채팅**

## **회원가입/로그인**
* **[회원가입]**
  
![회원가입gif](https://github.com/user-attachments/assets/0fb002fa-acf8-44d8-bb8b-81b964438d8b)

* **[소셜로그인_네이버]**
  
![네이버 로그인-최종gif](https://github.com/user-attachments/assets/a7e9bfff-860b-4121-8ebd-7d2a255a5e03)

* **[소셜로그인_구글]**
  
![구글 로그인](https://github.com/user-attachments/assets/f5a0a329-83d1-4bf0-9de3-382e9661baf1)

* **[소셜로그인_카카오]**

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
- ElasticSearch를 적용하며 초기 설정과 데이터 매핑, 쿼리 최적화 과정에서 많은 시행착오가 있었습니다. 그러나 공식 문서 참조, 지속적인 문제해결을 통해 성공적으로 ElasticSearch를 적용할 수 있었습니다. 또한 코드 병합시 충돌이 잦아 프로젝트의 안정성을 유지하는데 어려움이 있었지만, 팀원들과의 긴밀한 협력과 문제해결을 통해 이를 극복할 수 있었습니다. 이번 프로젝트를 통해 기술적 성장뿐만 아니라 협업의 중요성을 느낄 수 있었습니다. 추가적으로 프로젝트를 보완하여 더 나은 서비스를 만드는 것이 목표입니다.

> 오형동
- 파이널 프로젝트에서는 6명이서 시작했지만, 프로젝트 시작 전에 2명이 팀을 떠나 다른 팀에 비해 팀원 수가 부족한 상태로 진행하게 되었습니다. 그러나 남은 팀원들이 모두 열정적이고 협력적이어서, 팀워크를 통해 프로젝트를 성공적으로 마무리할 수 있었습니다. 특히, 멘토님들과 강사님들의 피드백을 통해 프로젝트를 진행하면서 많은 배움을 얻었습니다.
이번 프로젝트에서 Spring Batch 기술을 처음 접하게 되었고, 기초적인 이론부터 차근차근 공부하며 프로젝트에 적용하는 데 시간이 오래 걸렸습니다. 이로 인해 다른 팀원들을 충분히 도와주지 못한 부분이 아쉬움으로 남습니다. 이 경험을 통해, 다음 프로젝트에서는 새로운 기술을 접할 때 필요한 부분만 집중적으로 공부하여 시간을 효율적으로 활용하는 것이 중요하다는 것을 깨달았습니다.
비록 팀원 수가 적어 어려움이 있었지만, 좋은 팀원들과 함께 서로 의기투합하여 프로젝트를 성공적으로 마칠 수 있었습니다. 이번 경험은 협업의 중요성과 새로운 기술에 대한 효율적인 학습 방법을 배우는 귀중한 기회가 되었으며, 앞으로의 프로젝트에서도 이를 잘 활용할 것입니다.
> 박수빈

> 전현선
- 지금까지 진행했던 미니 프로젝트와 달리, 이번 파이널 프로젝트는 기획부터 배포까지 모든 과정을 팀원들과 함께 결정하고 작업하면서 많은 것을 배울 수 있었습니다. 특히, 강사님과 멘토님들의 피드백을 통해 부족한 부분을 수정하며 프로젝트를 더욱 발전시킬 수 있었습니다. 이번 프로젝트에서는 주제 외에도 기획, 설계, 개발, 배포까지 모든 과정을 팀원들과 함께 결정하며 진행했습니다. 많은 시행착오가 있었지만, 그 과정에서 다양한 문제를 해결하며 성장할 수 있었습니다. 특히, 팀원 수가 다른 팀들에 비해 부족한 상황에서 시작했지만, 서로의 강점을 살리며 협력하여 프로젝트를 성공적으로 마칠 수 있었습니다. 새로운 기능으로는 STOMP를 활용한 채팅 기능을 구현했으며, 이를 통해 실시간 통신 기술에 대한 이해를 넓힐 수 있었습니다. 이번 프로젝트를 통해 팀워크의 중요성을 다시 한번 깨달았고, 부족한 인원 속에서도 좋은 팀원들과 함께 많은 것을 배우며 성장할 수 있었습니다. 앞으로도 이러한 경험을 바탕으로 더욱 효율적이고 협력적인 프로젝트를 진행할 수 있을 것 같습니다.
