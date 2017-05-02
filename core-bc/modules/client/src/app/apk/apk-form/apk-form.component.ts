import {Component, Input, OnInit} from "@angular/core";
import {Data} from "../../model/data";
import {animate, state, style, transition, trigger} from "@angular/animations";
import {Http, RequestOptions, Headers, Response} from "@angular/http";
import {Ao3} from "../../model/ao3";
import {AbstractControl, FormBuilder, FormGroup, NgForm, ValidatorFn, Validators} from "@angular/forms";
import {Observable} from "rxjs/Observable";
import "rxjs/add/operator/mergeMap";
import "rxjs/add/operator/concatMap";
import "rxjs/add/observable/from";
import {Vardform} from "../../model/vardform";
import {Verksamhet} from "../../model/verksamhet";
import {MdSnackBar} from "@angular/material";
import {ErrorHandler} from "../../shared/error-handler";
import {Prodn1} from "../../model/prodn1";
import {Prodn2} from "../../model/prodn2";
import {Prodn3} from "../../model/prodn3";


@Component({
  selector: 'apk-form',
  templateUrl: './apk-form.component.html',
  styleUrls: ['./apk-form.component.css'],
  animations: [
    trigger('slideIn', [
      state('*', style({opacity: 0})),
      state('in', style({opacity: 1, transform: 'translateX(0)', height: 'auto'})),
      state('out', style({opacity: 0, height: '0', 'margin-bottom': 0, padding: 0})),
      transition('* => *', animate('.2s ease'))
    ])
  ]
})
export class ApkFormComponent implements OnInit {

  @Input('dataId') dataId: string;

  apkForm: FormGroup;

  data: Data;
  unitSearchResult: any; // todo Make typed

  allAo3s: Ao3[];
  allVardforms: Vardform[];
  allVerksamhets: Verksamhet[];
  allProdn1s: Prodn1[];

  filteredAo3Options: Observable<Ao3[]>;
  filteredVardformOptions: Observable<Vardform[]>;
  filteredVerksamhetOptions: Observable<Verksamhet[]>;

  prodn2Options: Prodn2[];
  prodn3Options: Prodn3[];

  ao3IdMap: Map<string, Ao3>;
  vardformIdMap: Map<string, Vardform>;
  verksamhetIdMap: Map<string, Verksamhet>;

  isPrivate: boolean;

  saveMessage: string;

  constructor(private http: Http,
              private formBuilder: FormBuilder,
              private snackBar: MdSnackBar,
              private errorHandler: ErrorHandler) {
  }

  ngOnInit() {

    this.data = new Data();

    let dataObservable: Observable<Data>;
    if (this.dataId) {
      dataObservable = this.http.get('/api/data/' + this.dataId).map(response => response.json());
    } else {
      dataObservable = Observable.from([new Data()]);
    }

    let ao3Observable = this.http.get('/api/ao3').map(response => response.json());
    let vardformObservable = this.http.get('/api/vardform').map(response => response.json());
    let verksamhetObservable = this.http.get('/api/verksamhet').map(response => response.json());
    let prodn1 = this.http.get('/api/prodn1').map(response => response.json());

    Observable.forkJoin([ao3Observable, vardformObservable, verksamhetObservable, dataObservable, prodn1])
      .subscribe((result: any[]) => {
        const ao3s = result[0];
        const vardforms = result[1];
        const verksamhets = result[2];

        const data = result[3];

        const prodn1s = result[4]; // These won't change as opposed to prodn2 and prodn3 which may change.

        this.allAo3s = ao3s;
        this.allVardforms = vardforms;
        this.allVerksamhets = verksamhets;

        this.data = data;

        this.allProdn1s = prodn1s;

        this.isPrivate = this.data.agarform === '4' || this.data.agarform === '5' || this.data.agarform === '6';

        let tempMap: Map<string, any> = new Map();
        for (let ao3 of ao3s) {
          tempMap.set(ao3.ao3id, ao3);
        }
        this.ao3IdMap = tempMap;

        tempMap = new Map();
        for (let vardform of vardforms) {
          tempMap.set(vardform.vardformid, vardform);
        }
        this.vardformIdMap = tempMap;

        tempMap = new Map();
        for (let verksamhet of verksamhets) {
          tempMap.set(verksamhet.verksamhetid, verksamhet);
        }
        this.verksamhetIdMap = tempMap;

        this.buildForm();

        this.initAo3FormControl(ao3s);
        this.initVardformControl();
        this.initVerksamhetControl();
      });
  }

  private buildForm() {

    this.apkForm = this.formBuilder.group({
      'unitSearch': [],
      'arbetsplatskod': [{value: this.data.arbetsplatskod, disabled: true}, []],
      'lankod': [this.data.lankod, Validators.required],
      'agarform': [this.data.agarform],
      'ao3': [this.ao3IdMap.get(this.data.ao3), Validators.required],
      'frivilligUppgift': [{value: this.data.frivilligUppgift, disabled: !this.isPrivate}],
      'ansvar': [this.data.ansvar, Validators.required],
      'vardform': [this.vardformIdMap.get(this.data.vardform), Validators.required],
      'verksamhet': [this.verksamhetIdMap.get(this.data.verksamhet), Validators.required],
      'sorteringsniva1': [null, Validators.required], // Set further down
      'sorteringsniva2': [null, Validators.required], // Set further down
      'sorteringsniva3': [null, Validators.required], // Set further down
      'benamning': [this.data.benamning, Validators.required],
      'addressGroup': this.formBuilder.group({
        'postadress': [this.data.postadress],
        'postnr': [this.data.postnr],
        'postort': [this.data.postort],
      }),

      'externfakturaGroup': this.formBuilder.group({
        'externfakturamodell': [this.data.externfakturamodell, Validators.required]
      }),
      'groupCode': [false, Validators.required],
      'vgpvGroup': this.formBuilder.group({
        'vgpv': [this.data.vgpv, Validators.required]
      }),
      'anmarkning': [this.data.anmarkning],
      'hsaid': [this.data.hsaid, Validators.required],
      'fromDatum': [this.data.fromDatum, Validators.compose([datePattern(), Validators.required])],
      'noTillDatum': [!this.data.tillDatum || this.data.tillDatum.length === 0],
      'tillDatum': [{
        value: this.data.tillDatum,
        disabled: !this.data.tillDatum || this.data.tillDatum.length == 0
      }, Validators.compose([datePattern(), Validators.required])]
    });

    this.apkForm.get('unitSearch').valueChanges
      .filter(value => {
        let requiredLength = value && value.length >= 3;

        if (!requiredLength) {
          this.unitSearchResult = [];
        }

        return requiredLength
      })
      .flatMap(query => {
        return this.http.get('/api/search/unit?query=' + query);
      })
      .map(result => result.json())
      .subscribe(
        result => this.unitSearchResult = result,
        error => this.errorHandler.notifyError(error)
      );

    this.apkForm.statusChanges.subscribe(value => {
      if (value === 'VALID') {
        this.saveMessage = '';
      }
    });

    this.apkForm.get('noTillDatum').valueChanges
      .subscribe((checked: boolean) => {
        if (checked) {
          this.apkForm.get('tillDatum').disable();
        } else {
          this.apkForm.get('tillDatum').enable();
        }
      });

    this.apkForm.get('groupCode').valueChanges
      .subscribe((checked: boolean) => {
        if (checked) {
          this.apkForm.get('addressGroup').disable();
        } else {
          this.apkForm.get('addressGroup').enable();
        }
      });

    this.apkForm.get('agarform').valueChanges
      .subscribe((value: string) => {
        this.isPrivate = value === '4' || value === '5' || value === '6';
        // this.isPrivate = this.apkForm.value.agarform === '4' || this.apkForm.value.agarform === '5' || this.apkForm.value.agarform === '6';
        if (this.isPrivate) {
          this.apkForm.get('frivilligUppgift').enable();
        } else {
          this.apkForm.get('frivilligUppgift').disable();
        }
      });

    if (this.data.sorteringskodProd) {
      this.http.get('/api/prodn3/' + this.data.sorteringskodProd)
        .map(response => response.json())
        .subscribe(prodn3 => {
          this.apkForm.patchValue({'sorteringsniva3': prodn3.producentid});

          this.initSorteringsnivaControls(prodn3);
        }, error => this.errorHandler.notifyError(error));
    } else {
      this.initSorteringsnivaControls(null);
    }
  }

  private initVerksamhetControl() {
    let verksamhetFormControl = this.apkForm.get('verksamhet');

    this.filteredVerksamhetOptions = verksamhetFormControl.valueChanges
      .startWith(null)
      .map((name: string) => name ? this.filterVerksamhet(name) : this.allVerksamhets.slice());
  }

  private initVardformControl() {
    let vardformFormControl = this.apkForm.get('vardform');

    this.filteredVardformOptions = vardformFormControl.valueChanges
      .startWith(null)
      .map((name: string) => name ? this.filterVardform(name) : this.allVardforms.slice());
  }

  private initAo3FormControl(ao3s) {
    let ao3FormControl = this.apkForm.get('ao3');

    this.filteredAo3Options = ao3FormControl.valueChanges // Side effect
      .startWith(null)
      // .map(ao3 => ao3 && typeof ao3 === 'object' ? ao3.foretagsnamn : ao3)
      .map((name: string) => name ? this.filterAo3(name) : this.allAo3s.slice());

    let map: Map<string, Ao3> = new Map();
    for (let ao3 of ao3s) {
      map.set(this.displayAo3Fn(ao3), ao3);
    }

    ao3FormControl.valueChanges.subscribe(value => {
      if (typeof value === 'string') {
        if (map.has(value)) {
          ao3FormControl.setValue(map.get(value));
        }
      }
    });

    ao3FormControl.setValidators(ao3Validator(ao3s))
  }

  private initSorteringsnivaControls(prodn3: any /*todo make typed*/) {
    if (prodn3) {
      // We assume the form is already built, so we don't need to fetch the prodn3 again.
      let prodn2Key = prodn3.n2;

      let prodn2;
      let prodn1;

      // Now we now prodn3. Based on that we want to find prodn2, prodn1 as well as options for prodn2 and prodn3.
      // Start by finding the specific prodn2
      this.http.get('/api/prodn2/' + prodn2Key)
        .map(response => response.json())
        .concatMap(prodn2 => {
          this.apkForm.patchValue({'sorteringsniva2': prodn2.producentid});

          // From this prodn2 we can find the chosen prodn1 but also the options for prodn3.
          let prodn1Observable: Observable<Response> = this.http.get('/api/prodn1/' + prodn2.n1)
            .map(response => response.json());

          let prodn3sObservable:  Observable<Response> = this.http.get('/api/prodn3?prodn2=' + prodn2.producentid)
            .map(response => response.json());

          return Observable.forkJoin(prodn1Observable, prodn3sObservable);
        })
        .concatMap((result: any[]) => {
          let prodn1 = result[0];
          let prodn3s = result[1];

          this.apkForm.patchValue({'sorteringsniva1': prodn1.producentid});
          this.prodn3Options = prodn3s;

          // Moving on with finding the options for prodn2, based on prodn1
          return this.http.get('/api/prodn2?prodn1=' + prodn1.producentid);
        })
        .map(response => response.json())
        .subscribe(prodn2s => {
          this.prodn2Options = prodn2s;
          this.listenToChangesToProdnx();
        }, error => this.errorHandler.notifyError(error));
    } else {
      this.listenToChangesToProdnx();
    }
  }

  listenToChangesToProdnx() {
    let sorteringsniva1Control = this.apkForm.get('sorteringsniva1');
    let sorteringsniva2Control = this.apkForm.get('sorteringsniva2');

    sorteringsniva1Control.valueChanges
      .filter(value => value ? true : false)
      .flatMap(prodn1Producentid =>
        this.http.get('/api/prodn2?prodn1=' + prodn1Producentid).map(response => response.json()))
      .subscribe(prodn2s => {
        this.prodn2Options = prodn2s;
        this.prodn3Options = [];
        this.apkForm.patchValue({
          'sorteringsniva2': null,
          'sorteringsniva3': null
        });
      }, error => this.errorHandler.notifyError(error));

    sorteringsniva2Control.valueChanges
      .filter(value => value ? true : false)
      .flatMap(prodn2Producentid =>
        this.http.get('/api/prodn3?prodn2=' + prodn2Producentid).map(response => response.json()))
      .subscribe(prodn3s => {
        this.prodn3Options = prodn3s;
        this.apkForm.patchValue({
          'sorteringsniva3': null
        });
      }, error => this.errorHandler.notifyError(error));
  }

  save() {
    if (!this.apkForm.valid) {
      this.saveMessage = 'Alla fält är inte korrekt ifyllda.';
      return;
    }

    let data = this.data;
    let formModel = this.apkForm.value;
    data.lankod = formModel.lankod;
    data.agarform = formModel.agarform;
    data.ao3 = (<Ao3> formModel.ao3).ao3id;
    data.anmarkning = formModel.anmarkning;
    data.ansvar = formModel.ansvar;
    data.frivilligUppgift = formModel.frivilligUppgift;
    data.hsaid = formModel.hsaid;
    data.vardform = (<Vardform> formModel.vardform).vardformid;
    data.verksamhet = (<Verksamhet> formModel.verksamhet).verksamhetid;
    data.benamning = formModel.benamning;
    data.postadress = formModel.addressGroup.postadress;
    data.postnr = formModel.addressGroup.postnr;
    data.postort = formModel.addressGroup.postort;
    data.externfakturamodell = formModel.externfakturaGroup.externfakturamodell;
    data.sorteringskodProd = formModel.sorteringsniva3;
    data.vgpv = formModel.vgpvGroup.vgpv;
    data.fromDatum = formModel.fromDatum;
    data.tillDatum = formModel.tillDatum;

    let headers = new Headers({'Content-Type': 'application/json'});
    let options = new RequestOptions({headers: headers});

    this.http.put('/api/data', JSON.stringify(data), options)
      .map(response => response.json())
      .subscribe((data: Data) => {
        this.data = data;
        this.buildForm();
        this.snackBar.open('Lyckades spara!', null, {duration: 3000});
      }, error => this.errorHandler.notifyError(error));
  }

  resetForm() {
    this.apkForm.reset();
    this.buildForm();
  }

  filterAo3(name: string): Ao3[] {
    return this.allAo3s.filter(option =>
    new RegExp(name, 'gi').test(option.foretagsnamn) ||
    new RegExp(name, 'gi').test(option.ao3id) ||
    new RegExp(name, 'gi').test(this.displayAo3Fn(option)));
  }

  filterVardform(name: string): Vardform[] {
    return this.allVardforms.filter(option =>
    new RegExp(name, 'gi').test(option.vardformtext) ||
    new RegExp(name, 'gi').test(option.vardformid) ||
    new RegExp(name, 'gi').test(this.displayVardformFn(option)));
  }

  filterVerksamhet(name: string): Verksamhet[] {
    return this.allVerksamhets.filter(option =>
    new RegExp(name, 'gi').test(option.verksamhettext) ||
    new RegExp(name, 'gi').test(option.verksamhetid) ||
    new RegExp(name, 'gi').test(this.displayVerksamhetFn(option)));
  }

  displayAo3Fn(ao3: Ao3): string {
    return ao3 ? ao3.ao3id + ' - ' + ao3.foretagsnamn : '';
  }

  displayVardformFn(vardform: Vardform): string {
    return vardform ? vardform.vardformid + ' - ' + vardform.vardformtext : '';
  }

  displayVerksamhetFn(verksamhet: Verksamhet): string {
    return verksamhet ? verksamhet.verksamhetid + ' - ' + verksamhet.verksamhettext : '';
  }

  displayUnitFn(unit: any): string {
    return unit && unit.attributes && unit.attributes.ou && unit.attributes.ou.length > 0 ? unit.attributes.ou[0] : '';
  }

  selectUnit(unit: any): void {
    if (unit.attributes.hsaStreetAddress && unit.attributes.hsaStreetAddress.length > 0) {
      let parts = unit.attributes.hsaStreetAddress[0].split('$');

      if (parts.length >= 3) {
        this.apkForm.patchValue({
          'addressGroup': {
            'postadress': parts[0],
            'postnr': parts[1],
            'postort': parts[2]
          }
        })
      }
    }

    this.apkForm.patchValue({
      'lankod': unit.attributes.hsaCountyCode,
      'ao3': unit.attributes.vgrAo3kod ? this.ao3IdMap.get(unit.attributes.vgrAo3kod[0]) : null,
      'hsaid': unit.attributes.hsaIdentity ? unit.attributes.hsaIdentity[0] : null,
      'benamning': unit.attributes.ou ? unit.attributes.ou[0] : null,
      'ansvar': unit.attributes.vgrAnsvarsnummer ? unit.attributes.vgrAnsvarsnummer[0] : null,
    });

    this.apkForm.get('lankod').markAsTouched();
    this.apkForm.get('ao3').markAsTouched();
    this.apkForm.get('hsaid').markAsTouched();
    this.apkForm.get('benamning').markAsTouched();
    this.apkForm.get('ansvar').markAsTouched();
    this.apkForm.get('addressGroup').markAsTouched();
  }
}

export function ao3Validator(ao3s: Ao3[]): ValidatorFn {
  return (control: AbstractControl): { [key: string]: any } => {
    return ao3s.indexOf(control.value) === -1 ? {'invalidName': control.value} : null;
  };
}

export function datePattern(): ValidatorFn {
  return (control: AbstractControl): { [key: string]: any } => {
    let datePattern = /^\d{4}-\d{2}-\d{2}$/;

    if (!control.value || !control.value.match(datePattern)) {
      return {"datePattern": true};
    }

    return null;
  }
}