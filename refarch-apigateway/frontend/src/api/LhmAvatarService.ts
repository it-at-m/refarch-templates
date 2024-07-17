export class LhmAvatarService {
  base: string;
  constructor(base: string) {
    this.base = base;
  }

  /**
   *
   * @param username UID (vorname.nachname)
   * @param mode Mode, default: fallbackGeneric, see https://github.com/it-at-m/ad2image#using-the-api
   * @param size Size, default: 64, see https://github.com/it-at-m/ad2image#using-the-api
   * @returns href zum Abruf des Avatars zum User
   */
  avatarHref(username: string, mode = "fallbackGeneric", size = "64"): string {
    const url = new URL("/avatar", this.base);
    url.searchParams.append("uid", username);
    url.searchParams.append("m", mode);
    url.searchParams.append("size", size);
    return url.toString();
  }
}

/**
 * Der Default-LhmAvatarService, der mucatar.muenchen.de verwendet.
 *
 * @see https://git.muenchen.de/km23/mucatar
 * @see https://github.com/it-at-m/ad2image#documentation
 */
export const DefaultLhmAvatarService = new LhmAvatarService(
  "https://mucatar.muenchen.de/"
);
