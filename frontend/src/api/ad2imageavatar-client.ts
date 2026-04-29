import { AD2IMAGE_URL } from "@/constants";

/**
 *
 * @param username UID (firstname.lastname)
 * @param mode Mode, default: fallbackGeneric, see https://github.com/it-at-m/ad2image#using-the-api
 * @param size Size, default: 64, see https://github.com/it-at-m/ad2image#using-the-api
 * @returns href to retrieve the avatar for the user
 */
export function getAvatarHref(
  username: string,
  mode = "fallbackGeneric",
  size = "64"
): string {
  const baseUrl = new URL(AD2IMAGE_URL);
  const basePath = baseUrl.pathname.endsWith("/")
    ? baseUrl.pathname
    : `${baseUrl.pathname}/`;
  baseUrl.pathname = `${basePath}avatar`;
  const url = baseUrl;
  url.searchParams.append("uid", username);
  url.searchParams.append("m", mode);
  url.searchParams.append("size", size);
  return url.toString();
}
