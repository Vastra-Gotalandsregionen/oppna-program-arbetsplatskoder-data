import {StateService} from '../../core/state/state.service';
import {Util} from "../../core/util/util";
import {Data} from '../../model/data';

export class ApkBase {

  stateService : StateService

  constructor(stateService : StateService) {
    this.stateService = stateService;
  }

  getShowDebug(): boolean {
    return this.stateService.showDebug;
  }

  getStatus(data: Data) {

    const tillDatum = data.tillDatum;

    if (!tillDatum) {
      // No value means "until further notice".
      return 'valid';
    }

    if (Util.isOlderThanXYears(tillDatum, 1)) {
      return 'fullyClosed';
    } else if (Util.isOlderThanXYears(tillDatum, 0)) {
      return 'closed'
    } else {
      return 'valid';
    }

  }

}
