import {NativeDateAdapter} from "@angular/material"

export class MyDateAdapter extends NativeDateAdapter {

  getFirstDayOfWeek(): number {
    return 1; // Monday
  }
}
