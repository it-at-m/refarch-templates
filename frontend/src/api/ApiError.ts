import { STATUS_INDICATORS } from "@/constants";

export class ApiError extends Error {
  level: string;
  constructor({
    level = STATUS_INDICATORS.ERROR,
    message = "Ein unbekannter Fehler ist aufgetreten, bitte den Administrator informieren.",
  }: {
    level?: string;
    message?: string;
  }) {
    // Passes the remaining parameters (including vendor-specific parameters) to the error constructor
    super(message);

    // Retains the correct stack trace for the point at which the error was triggered
    this.stack = new Error().stack;

    // User-defined information
    this.level = level;
    this.message = message;
  }
}
