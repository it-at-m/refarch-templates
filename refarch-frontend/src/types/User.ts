class User {
  sub = "";

  // LHM
  displayName = "";
  surname = "";
  telephoneNumber = "";
  email = "";
  username = "";
  givenname = "";
  department = "";
  lhmObjectID = "";
  // LHM_Extended
  preferred_username = "";
  memberof: string[] = [];
  user_roles: string[] = [];
  authorities: string[] = [];

  static fromJson(json: Partial<User>): User {
    const u = new User();
    u.sub = json.sub || "";

    // LHM
    u.displayName = json.displayName || "";
    u.surname = json.surname || "";
    u.telephoneNumber = json.telephoneNumber || "";
    u.email = json.email || "";
    u.username = json.username || "";
    u.givenname = json.givenname || "";
    u.department = json.department || "";
    u.lhmObjectID = json.lhmObjectID || "";

    // LHM_Extended
    u.preferred_username = json.preferred_username || "";
    u.memberof = json.memberof || [];
    u.user_roles = json.user_roles || [];
    u.authorities = json.authorities || [];
    return u;
  }
}

export function UserLocalDevelopment(): User {
  const u = new User();
  u.username = "Local Development User";
  u.displayName = "Local Development User";
  u.authorities = [
    // todo add authorities
  ];
  u.user_roles = [
    "writer", // Add writer role for local development
  ];
  return u;
}

export default User;
