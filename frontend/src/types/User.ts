export interface User {
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

export const USER_LOCAL_DEVELOPMENT: User = {
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

export const USER_EMPTY: User = {
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
