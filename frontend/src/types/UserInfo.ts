export interface UserInfo {
  // scope: basic
  sub: string;

  // scope: profile
  preferred_username: string;
  name: string;
  given_name: string;
  family_name: string;

  // scope: email
  email: string;

  // scope: roles
  resource_access: Record<string, { roles: string[] }>;

  // scope: phone
  phone_number: string;

  // scope: lhm-core
  department: string;
  lhmObjectID: string;
}

export const USERINFO_LOCAL_DEVELOPMENT: UserInfo = {
  sub: "",

  preferred_username: "local.development-user",
  name: "Local Development-User",
  given_name: "Local",
  family_name: "Development-User",

  email: "local@development.com",

  telephone_number: "+1234567890",

  department: "Local Department",
  lhmObjectID: "LOCAL_ID",

  resource_access: {
    local: {
      roles: ["writer", "reader"],
    },
  },
};

export const USERINFO_EMPTY: UserInfo = {
  sub: "",

  preferred_username: "",
  name: "",
  given_name: "",
  family_name: "",

  email: "",

  telephone_number: "",

  department: "",
  lhmObjectID: "",

  resource_access: {},
};
