export interface UserInfo {
  sub: string;

  // LHM
  displayName: string;
  surname: string;
  telephoneNumber: string;
  email: string;
  username: string;
  givenname: string;
  department: string;
  lhmObjectID: string;

  // LHM_Extended
  preferred_username: string;
  memberof: string[];
  user_roles: string[];
  authorities: string[];
}

export const USERINFO_LOCAL_DEVELOPMENT: UserInfo = {
  sub: "",

  // LHM
  displayName: "Local Development User",
  surname: "User",
  telephoneNumber: "+1234567890",
  email: "local@development.com",
  username: "Local Development User",
  givenname: "Local",
  department: "Local Department",
  lhmObjectID: "LOCAL_ID",

  // LHM_Extended
  preferred_username: "",
  memberof: [],
  user_roles: [
    // todo add authorities
  ],
  authorities: [
    // todo add user roles
  ],
};

export const USERINFO_EMPTY: UserInfo = {
  sub: "",

  // LHM
  displayName: "",
  surname: "",
  telephoneNumber: "",
  email: "",
  username: "",
  givenname: "",
  department: "",
  lhmObjectID: "",

  // LHM_Extended
  preferred_username: "",
  memberof: [],
  user_roles: [],
  authorities: [],
};
