window.swaggerSpec={
  "openapi" : "3.0.1",
  "info" : {
    "title" : "Unban TMS Admin",
    "description" : "Unban TMS Admin API description",
    "version" : "0.1.0"
  },
  "servers" : [ {
    "url" : "http://localhost:8080"
  } ],
  "tags" : [ ],
  "paths" : {
    "/api/members/1" : {
      "post" : {
        "tags" : [ "User API", "api" ],
        "summary" : "Get all users",
        "description" : "Get all users",
        "operationId" : "get-memberget-usersmember-controller-test/find-member-by-word-test",
        "requestBody" : {
          "content" : {
            "application/json;charset=UTF-8" : {
              "schema" : {
                "$ref" : "#/components/schemas/api-members-1-79137062"
              },
              "examples" : {
                "get-users" : {
                  "value" : "{\n  \"id\" : 1\n}"
                },
                "member-controller-test/find-member-by-word-test" : {
                  "value" : "{\n  \"id\" : 1\n}"
                },
                "get-member" : {
                  "value" : "{\n  \"id\" : 1\n}"
                }
              }
            }
          }
        },
        "responses" : {
          "200" : {
            "description" : "200",
            "content" : {
              "application/json;charset=UTF-8" : {
                "schema" : {
                  "$ref" : "#/components/schemas/UserResponse"
                },
                "examples" : {
                  "get-users" : {
                    "value" : "{\"id\":1,\"name\":\"조현철1\",\"email\":\"kkensu@naver.com\"}"
                  },
                  "member-controller-test/find-member-by-word-test" : {
                    "value" : "{\n  \"id\" : 1,\n  \"name\" : \"조현철1\",\n  \"email\" : \"kkensu@naver.com\"\n}"
                  },
                  "get-member" : {
                    "value" : "{\"id\":1,\"name\":\"조현철1\",\"email\":\"kkensu@naver.com\"}"
                  }
                }
              }
            }
          }
        }
      }
    }
  },
  "components" : {
    "schemas" : {
      "api-members-1-79137062" : {
        "type" : "object",
        "properties" : {
          "id" : {
            "type" : "number",
            "description" : "회원 ID"
          }
        }
      },
      "UserResponse" : {
        "title" : "UserResponse",
        "type" : "object"
      }
    }
  }
}