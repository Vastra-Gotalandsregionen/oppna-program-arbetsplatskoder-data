export class Util {

  public static isOlderThanXYears(dateString: string, years: number) {
    const date = new Date(dateString);

    const today = new Date();

    const xYearsAgo = new Date();
    xYearsAgo.setFullYear(today.getFullYear() - years);

    this.toBeginningOfDay(xYearsAgo);
    this.toBeginningOfDay(date);

    if (date.getTime() < xYearsAgo.getTime()) {
      return true;
    }

    return false;
  }

  private static toBeginningOfDay(date: Date) {
    date.setHours(0);
    date.setMinutes(0);
    date.setSeconds(0);
    date.setMilliseconds(0);
  }
}
