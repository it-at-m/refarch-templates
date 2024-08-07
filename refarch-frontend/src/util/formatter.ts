export function toFirstLetterUppercase(text: string): string {
  return text ? text[0].toUpperCase() + text.slice(1).toLowerCase() : "";
}

export function toDateString(date: Date): string {
  return date ? date.toLocaleDateString() : "";
}

export function toTimeString(date: Date): string {
  return date ? date.toLocaleTimeString() : "";
}

export function toDateAndTimeString(date: Date): string {
  return date ? date.toLocaleString() : "";
}
