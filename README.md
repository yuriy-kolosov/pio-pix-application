# Pio-pix
## Простая система управления данными пользователей и перевода средств
## Демонстрационный вариант

2026-03-17
____
Автор: Юрий Колосов

Наименование проекта в GitHub:
- pio-pix-auth-server (pio-pix-authorization-server):

  https://github.com/yuriy-kolosov/pio-pix-auth-server.git
  
- **pio-pix-application (Backend Application):**

  **https://github.com/yuriy-kolosov/pio-pix-application.git**
____
## Общее описание

Простая система управления данными пользователей и перевода средств обеспечивает возможность изменения данных пользователей и выполнения переводов средств между ними

Данный проект является демонстрацией API
____
### Описание сервиса:
- Поддерживается аутентификация и авторизация пользователей по email и паролю
- Аутентификация доступа к API осуществляется с помощью JWT-токена
  
- Пользователь (USER) имеет возможность:
  
  -- управлять относящимися к нему данными (изменять, добавлять и удалять)
  
  -- просматривать список всех пользователей

  -- просматривать данные отдельных пользователей
  
  -- выполнять переводы средств

  -- просматривать данные о выполненных переводах средств
____

## Инструкция для локального запуска проекта

### 1 Клонировать исходный код проекта из репозитория GitHub на локальный компьютер: выполнить команды:

- git clone https://github.com/yuriy-kolosov/pio-pix-application.git I:\Tasks\Pioneer_Pixel\pio-pix\pio-pix
- git clone https://github.com/yuriy-kolosov/pio-pix-auth-server.git I:\Tasks\Pioneer_Pixel\pio-pix\pio-pix-auth-server

### 2 В локальном каталоге <путь к локальному каталогу> \ <имя каталога для клонирования> выполнить команду:
- docker compose up

### 3 Для входа в систему с ролью USER (scope = openid USER) - в браузере локального компьютера выполнить следующий запрос:
- http://localhost:8080/oauth2/authorize?response_type=code&client_id=client&scope=openid%20USER&redirect_uri=http://localhost:9090/pio-pix/authorized&code_challenge=tqtVKD0f_jp-O9Z2-iCFgGtussb5Lh55nBsEFz2gPb8&code_challenge_method=S256

### 4 На странице входа ввести email и пароль пользователя (используется демонстрационный вариант):
- логин: user1@pio-pix.ru
- пароль: user1

### 5 Выполнить копирование кода авторизации со страницы, на которую произведено перенаправление, - из строки формата:
- http://localhost:9090/pio-pix/authorized?code=<код_авторизации>

### 6 Выполнить запрос токена доступа с использованием Postman и полученного кода авторизации:
- POST http://localhost:8080/oauth2/token?client_id=client&redirect_uri=http://localhost:9090/pio-pix/authorized&grant_type=authorization_code&code=<код_авторизации>&code_verifier=L21TeFEVuOw_lWfi8pkvgqldcjZSDJdVtT4qvJBF7Do
- Применяется Basic Auth с параметрами: Username=client, Password=secret (демонстрационный вариант)

### 7 С использованием полученного токена произвести в Postman проверку функционирования API (роль ADMIN) на основании приведенного ниже описания (файл openapi.yaml) и/или с использованием следующих примеров запросов:

- Запрос информации о пользователе user1 по email:
- GET http://localhost:9090/user/email?userEmail=user1@piopix.ru

- Запрос информации о пользователе user1 по номеру телефона:
- GET http://localhost:9090/user/phone?userPhone=79307865433

- Запрос постраничного списка пользователей по начальным символам имени:
- GET http://localhost:9090/user/name/by-pages?userName=user&pageNumber=1&pageAmount=5

- Запрос информации о пользователях с указанной датой рождения или старше:
- GET http://localhost:9090/user/date-of-birth/by-pages?dateOfBirth=1993-05-02&pageNumber=1&pageAmount=5

- Добавление нового email текущему пользователю (user1):
- POST http://localhost:9090/user/email?userEmail=user1_2@piopix.ru

- Добавление нового номера телефона текущему пользователю (user1):
- POST http://localhost:9090/user/phone?userPhone=79117865432

- Изменение email текущего пользователя (user1):
- PUT http://localhost:9090/user/email?userEmail=user1_2@piopix.ru
-- Body: { "id": 1, "userId": 1, "email": "user1_2_3@piopix.ru" }

- Изменение номера телефона текущего пользователя (user1):
- PUT http://localhost:9090/user/phone?userPhone=79117865432
-- Body: { "id": 1, "userId": 1, "phone": "79127865433" }

- Удаление email текущего пользователя (user1):
- DELETE http://localhost:9090/user/email?userEmail=user1_2_3@piopix.ru

- Удаление номера телефона текущего пользователя (user1):
- DELETE http://localhost:9090/user/phone?userPhone=79127865433

- Запрос информации о выполненных переводах средств:
- GET http://localhost:9090/transfer?pageNumber=1&pageAmount=2

- Перевод средств:
- POST http://localhost:9090/transfer?userToId=3&amount=20
____
### Описание интерфейса: файл openapi.yaml
```yaml
{
  "openapi": "3.0.1",
  "info": {
    "title": "OpenAPI definition",
    "version": "v0"
  },
  "servers": [
    {
      "url": "http://localhost:9090",
      "description": "Generated server url"
    }
  ],
  "paths": {
    "/user/phone": {
      "get": {
        "tags": [
          "Пользователи"
        ],
        "summary": "Получить информацию о пользователе по номеру телефона: точное совпадение",
        "operationId": "getUserByPhone",
        "parameters": [
          {
            "name": "userPhone",
            "in": "query",
            "required": true,
            "schema": {
              "pattern": "^79\\d{9}$",
              "type": "string"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/UserDTO"
                }
              }
            }
          },
          "400": {
            "description": "Bad Request",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/UserDTO"
                }
              }
            }
          },
          "401": {
            "description": "Unauthorized",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/UserDTO"
                }
              }
            }
          },
          "404": {
            "description": "Not Found",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/UserDTO"
                }
              }
            }
          }
        }
      },
      "put": {
        "tags": [
          "Пользователи"
        ],
        "summary": "Изменить номер телефона: доступно текущему пользователю",
        "operationId": "updatePhone",
        "parameters": [
          {
            "name": "userPhone",
            "in": "query",
            "required": true,
            "schema": {
              "pattern": "^79\\d{9}$",
              "type": "string"
            }
          }
        ],
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "$ref": "#/components/schemas/PhoneDataDTO"
              }
            }
          },
          "required": true
        },
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/PhoneDataDTO"
                }
              }
            }
          },
          "400": {
            "description": "Bad Request",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/PhoneDataDTO"
                }
              }
            }
          },
          "401": {
            "description": "Unauthorized",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/PhoneDataDTO"
                }
              }
            }
          },
          "404": {
            "description": "Not Found",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/PhoneDataDTO"
                }
              }
            }
          },
          "409": {
            "description": "Conflict",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/PhoneDataDTO"
                }
              }
            }
          }
        }
      },
      "post": {
        "tags": [
          "Пользователи"
        ],
        "summary": "Добавить новый номер телефона: доступно текущему пользователю",
        "operationId": "addNewPhone",
        "parameters": [
          {
            "name": "userPhone",
            "in": "query",
            "required": true,
            "schema": {
              "pattern": "^79\\d{9}$",
              "type": "string"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/PhoneDataDTO"
                }
              }
            }
          },
          "400": {
            "description": "Bad Request",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/PhoneDataDTO"
                }
              }
            }
          },
          "401": {
            "description": "Unauthorized",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/PhoneDataDTO"
                }
              }
            }
          },
          "404": {
            "description": "Not Found",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/PhoneDataDTO"
                }
              }
            }
          },
          "409": {
            "description": "Conflict",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/PhoneDataDTO"
                }
              }
            }
          }
        }
      },
      "delete": {
        "tags": [
          "Пользователи"
        ],
        "summary": "Удалить номер телефона: доступно текущему пользователю",
        "operationId": "deletePhone",
        "parameters": [
          {
            "name": "userPhone",
            "in": "query",
            "required": true,
            "schema": {
              "pattern": "^79\\d{9}$",
              "type": "string"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/PhoneDataDTO"
                }
              }
            }
          },
          "400": {
            "description": "Bad Request",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/PhoneDataDTO"
                }
              }
            }
          },
          "401": {
            "description": "Unauthorized",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/PhoneDataDTO"
                }
              }
            }
          },
          "404": {
            "description": "Not Found",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/PhoneDataDTO"
                }
              }
            }
          },
          "409": {
            "description": "Conflict",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/PhoneDataDTO"
                }
              }
            }
          }
        }
      }
    },
    "/user/email": {
      "get": {
        "tags": [
          "Пользователи"
        ],
        "summary": "Получить информацию о пользователе по email: точное совпадение",
        "operationId": "getUserByEmail",
        "parameters": [
          {
            "name": "userEmail",
            "in": "query",
            "required": true,
            "schema": {
              "pattern": "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
              "type": "string"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/UserDTO"
                }
              }
            }
          },
          "400": {
            "description": "Bad Request",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/UserDTO"
                }
              }
            }
          },
          "401": {
            "description": "Unauthorized",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/UserDTO"
                }
              }
            }
          },
          "404": {
            "description": "Not Found",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/UserDTO"
                }
              }
            }
          }
        }
      },
      "put": {
        "tags": [
          "Пользователи"
        ],
        "summary": "Изменить email: доступно текущему пользователю",
        "operationId": "updateEmail",
        "parameters": [
          {
            "name": "userEmail",
            "in": "query",
            "required": true,
            "schema": {
              "pattern": "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
              "type": "string"
            }
          }
        ],
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "$ref": "#/components/schemas/EmailDataDTO"
              }
            }
          },
          "required": true
        },
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/EmailDataDTO"
                }
              }
            }
          },
          "400": {
            "description": "Bad Request",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/EmailDataDTO"
                }
              }
            }
          },
          "401": {
            "description": "Unauthorized",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/EmailDataDTO"
                }
              }
            }
          },
          "404": {
            "description": "Not Found",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/EmailDataDTO"
                }
              }
            }
          },
          "409": {
            "description": "Conflict",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/EmailDataDTO"
                }
              }
            }
          }
        }
      },
      "post": {
        "tags": [
          "Пользователи"
        ],
        "summary": "Добавить новый email: доступно текущему пользователю",
        "operationId": "addNewEmail",
        "parameters": [
          {
            "name": "userEmail",
            "in": "query",
            "required": true,
            "schema": {
              "pattern": "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
              "type": "string"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/EmailDataDTO"
                }
              }
            }
          },
          "400": {
            "description": "Bad Request",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/EmailDataDTO"
                }
              }
            }
          },
          "401": {
            "description": "Unauthorized",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/EmailDataDTO"
                }
              }
            }
          },
          "404": {
            "description": "Not Found",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/EmailDataDTO"
                }
              }
            }
          },
          "409": {
            "description": "Conflict",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/EmailDataDTO"
                }
              }
            }
          }
        }
      },
      "delete": {
        "tags": [
          "Пользователи"
        ],
        "summary": "Удалить email: доступно текущему пользователю",
        "operationId": "deleteEmail",
        "parameters": [
          {
            "name": "userEmail",
            "in": "query",
            "required": true,
            "schema": {
              "pattern": "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
              "type": "string"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/EmailDataDTO"
                }
              }
            }
          },
          "400": {
            "description": "Bad Request",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/EmailDataDTO"
                }
              }
            }
          },
          "401": {
            "description": "Unauthorized",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/EmailDataDTO"
                }
              }
            }
          },
          "404": {
            "description": "Not Found",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/EmailDataDTO"
                }
              }
            }
          },
          "409": {
            "description": "Conflict",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/EmailDataDTO"
                }
              }
            }
          }
        }
      }
    },
    "/transfer": {
      "get": {
        "tags": [
          "Трансфер"
        ],
        "summary": "Получить информацию о выполненных переводах средств",
        "operationId": "getTransfer",
        "parameters": [
          {
            "name": "pageNumber",
            "in": "query",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32"
            }
          },
          {
            "name": "pageAmount",
            "in": "query",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/TransferDataDTO"
                }
              }
            }
          },
          "400": {
            "description": "Bad Request",
            "content": {
              "*/*": {
                "schema": {
                  "type": "array",
                  "items": {
                    "$ref": "#/components/schemas/TransferDataDTO"
                  }
                }
              }
            }
          },
          "401": {
            "description": "Unauthorized",
            "content": {
              "*/*": {
                "schema": {
                  "type": "array",
                  "items": {
                    "$ref": "#/components/schemas/TransferDataDTO"
                  }
                }
              }
            }
          },
          "404": {
            "description": "Not Found",
            "content": {
              "*/*": {
                "schema": {
                  "type": "array",
                  "items": {
                    "$ref": "#/components/schemas/TransferDataDTO"
                  }
                }
              }
            }
          }
        }
      },
      "post": {
        "tags": [
          "Трансфер"
        ],
        "summary": "Выполнить перевод средств на счет получателя",
        "operationId": "transfer",
        "parameters": [
          {
            "name": "userToId",
            "in": "query",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int64"
            }
          },
          {
            "name": "amount",
            "in": "query",
            "required": true,
            "schema": {
              "type": "number"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/TransferDataDTO"
                }
              }
            }
          },
          "400": {
            "description": "Bad Request",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/TransferDataDTO"
                }
              }
            }
          },
          "401": {
            "description": "Unauthorized",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/TransferDataDTO"
                }
              }
            }
          },
          "404": {
            "description": "Not Found",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/TransferDataDTO"
                }
              }
            }
          },
          "409": {
            "description": "Conflict",
            "content": {
              "*/*": {
                "schema": {
                  "$ref": "#/components/schemas/TransferDataDTO"
                }
              }
            }
          }
        }
      }
    },
    "/user/name/by-pages": {
      "get": {
        "tags": [
          "Пользователи"
        ],
        "summary": "Получить информацию о пользователе по имени: начальные символы (постраничная выборка)",
        "operationId": "getUserByName",
        "parameters": [
          {
            "name": "userName",
            "in": "query",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "pageNumber",
            "in": "query",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32"
            }
          },
          {
            "name": "pageAmount",
            "in": "query",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/UserDTO"
                }
              }
            }
          },
          "400": {
            "description": "Bad Request",
            "content": {
              "*/*": {
                "schema": {
                  "type": "array",
                  "items": {
                    "$ref": "#/components/schemas/UserDTO"
                  }
                }
              }
            }
          },
          "401": {
            "description": "Unauthorized",
            "content": {
              "*/*": {
                "schema": {
                  "type": "array",
                  "items": {
                    "$ref": "#/components/schemas/UserDTO"
                  }
                }
              }
            }
          }
        }
      }
    },
    "/user/date-of-birth/by-pages": {
      "get": {
        "tags": [
          "Пользователи"
        ],
        "summary": "Получить информацию о пользователе по дате рождения: позже указанной даты (постраничная выборка)",
        "operationId": "getUserByDateOfBirth",
        "parameters": [
          {
            "name": "dateOfBirth",
            "in": "query",
            "required": true,
            "schema": {
              "type": "string",
              "format": "date"
            }
          },
          {
            "name": "pageNumber",
            "in": "query",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32"
            }
          },
          {
            "name": "pageAmount",
            "in": "query",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "OK",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/UserDTO"
                }
              }
            }
          },
          "400": {
            "description": "Bad Request",
            "content": {
              "*/*": {
                "schema": {
                  "type": "array",
                  "items": {
                    "$ref": "#/components/schemas/UserDTO"
                  }
                }
              }
            }
          },
          "401": {
            "description": "Unauthorized",
            "content": {
              "*/*": {
                "schema": {
                  "type": "array",
                  "items": {
                    "$ref": "#/components/schemas/UserDTO"
                  }
                }
              }
            }
          }
        }
      }
    }
  },
  "components": {
    "schemas": {
      "PhoneDataDTO": {
        "required": [
          "id",
          "phone",
          "userId"
        ],
        "type": "object",
        "properties": {
          "id": {
            "type": "integer",
            "format": "int64"
          },
          "userId": {
            "type": "integer",
            "format": "int64"
          },
          "phone": {
            "pattern": "^79\\d{9}$",
            "type": "string"
          }
        }
      },
      "EmailDataDTO": {
        "required": [
          "email",
          "id",
          "userId"
        ],
        "type": "object",
        "properties": {
          "id": {
            "type": "integer",
            "format": "int64"
          },
          "userId": {
            "type": "integer",
            "format": "int64"
          },
          "email": {
            "pattern": "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            "type": "string"
          }
        }
      },
      "TransferDataDTO": {
        "required": [
          "amount",
          "id",
          "localDateTime",
          "userFromId",
          "userToId"
        ],
        "type": "object",
        "properties": {
          "id": {
            "type": "integer",
            "format": "int64"
          },
          "userFromId": {
            "type": "integer",
            "format": "int64"
          },
          "userToId": {
            "type": "integer",
            "format": "int64"
          },
          "amount": {
            "type": "number"
          },
          "localDateTime": {
            "type": "string",
            "format": "date-time"
          }
        }
      },
      "UserDTO": {
        "required": [
          "dateOfBirth",
          "id",
          "name"
        ],
        "type": "object",
        "properties": {
          "id": {
            "type": "integer",
            "format": "int64"
          },
          "name": {
            "type": "string"
          },
          "dateOfBirth": {
            "type": "string",
            "format": "date"
          }
        }
      }
    }
  }
}
