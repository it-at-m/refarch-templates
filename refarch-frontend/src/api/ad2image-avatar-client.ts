import { AD2IMAGE_URL } from "@/constants";

export class Ad2imageAvatarClient {
  base: string;
  constructor(base: string) {
    this.base = base;
  }

  /**
   *
   * @param username UID (firstname.lastname)
   * @param mode Mode, default: fallbackGeneric, see https://github.com/it-at-m/ad2image#using-the-api
   * @param size Size, default: 64, see https://github.com/it-at-m/ad2image#using-the-api
   * @returns href to retrieve the avatar for the user
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
 * The default Ad2imageAvatarClient, which is configurable via environment variable VITE_AD2IMAGE_URL in .env file.
 *
 * @see https://github.com/it-at-m/ad2image#documentation
 */
export const DefaultLhmAvatarService = new Ad2imageAvatarClient(AD2IMAGE_URL);
