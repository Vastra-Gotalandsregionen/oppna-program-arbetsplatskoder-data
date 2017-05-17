import {Component, Input, OnInit} from '@angular/core';
import {Location} from "@angular/common";
import {Data} from '../../model/data';
import {animate, state, style, transition, trigger} from '@angular/animations';
import {RequestOptions, Headers, Response} from '@angular/http';
import {Ao3} from '../../model/ao3';
import {AbstractControl, FormBuilder, FormGroup, ValidatorFn, Validators} from '@angular/forms';
import {Observable} from 'rxjs/Observable';
import {Vardform} from '../../model/vardform';
import {Verksamhet} from '../../model/verksamhet';
import {MdSnackBar} from '@angular/material';
import {StateService} from '../../core/state/state.service';
import {ErrorHandler} from '../../shared/error-handler';
import {Prodn1} from '../../model/prodn1';
import {Prodn2} from '../../model/prodn2';
import {Prodn3} from '../../model/prodn3';
import {JwtHttp} from '../../core/jwt-http';
import {RestResponse} from '../../model/rest-response';
import {ApkBase} from "../apk-base/apk-base";

@Component({
  selector: 'apk-form',
  templateUrl: './apk-form.component.html',
  styleUrls: ['./apk-form.component.scss'],
  animations: [
    trigger('slideIn', [
      state('*', style({opacity: 0})),
      state('in', style({opacity: 1, transform: 'translateX(0)', height: 'auto'})),
      state('out', style({opacity: 0, height: '0', 'margin-bottom': 0, padding: 0})),
      transition('* => *', animate('.2s ease'))
    ])
  ]
})
export class ApkFormComponent extends ApkBase implements OnInit {

  @Input('dataId') dataId: string;

  location: Location;
  stateService: StateService;

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

  constructor(private http: JwtHttp,
              private formBuilder: FormBuilder,
              private snackBar: MdSnackBar,
              location: Location,
              stateService: StateService,
              private errorHandler: ErrorHandler) {

      super(location, stateService);
      this.location = location;
      this.stateService = stateService;
  }

  ngOnInit() {

    const defaultData = new Data();

    // Default values:
    defaultData.externfakturamodell = 'nej';

    let dataObservable: Observable<Data>;
    if (this.dataId) {
      dataObservable = this.http.get('/api/data/' + this.dataId).map(response => response.json());
    } else {
      dataObservable = Observable.from([defaultData]);
    }

    const ao3Observable = this.http.get('/api/ao3').map(response => response.json().content);
    const vardformObservable = this.http.get('/api/vardform').map(response => response.json());
    const verksamhetObservable = this.http.get('/api/verksamhet').map(response => response.json());
    const prodn1 = this.http.get('/api/prodn1').map(response => response.json());

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
        for (const ao3 of ao3s) {
          tempMap.set(ao3.ao3id, ao3);
        }
        this.ao3IdMap = tempMap;

        tempMap = new Map();
        for (const vardform of vardforms) {
          tempMap.set(vardform.vardformid, vardform);
        }
        this.vardformIdMap = tempMap;

        tempMap = new Map();
        for (const verksamhet of verksamhets) {
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
      'agarform': [this.data.agarform],
      'ao3': [this.ao3IdMap.get(this.data.ao3), Validators.required],
      'frivilligUppgift': [{value: this.data.frivilligUppgift, disabled: !this.isPrivate}],
      'ansvar': [this.data.ansvar, Validators.required],
      'vardform': [this.vardformIdMap.get(this.data.vardform), Validators.required],
      'verksamhet': [this.verksamhetIdMap.get(this.data.verksamhet), Validators.required],
      'prodn1': [null, Validators.required], // Set further down
      'prodn2': [null, Validators.required], // Set further down
      'prodn3': [null, Validators.required], // Set further down
      'benamning': [this.data.benamning, Validators.required],
      'addressGroup': this.formBuilder.group({
        'postadress': [this.data.postadress],
        'postnr': [this.data.postnr],
        'postort': [this.data.postort],
      }),
      'externfakturaGroup': this.formBuilder.group({
        'externfakturamodell': [this.data.externfakturamodell, Validators.required]
      }),
      'groupCode': [this.data.groupCode],
      'vgpvGroup': this.formBuilder.group({
        'vgpv': [this.data.vgpv ? 'true' : 'false', Validators.required]
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
        const requiredLength = value && value.length >= 3;

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
        result => this.unitSearchResult = result
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

    if (this.data.prodn3) {
      this.initProdnControls(this.data.prodn3);
    } else {
      this.initProdnControls(null);
    }
  }

  private initVerksamhetControl() {
    const verksamhetFormControl = this.apkForm.get('verksamhet');

    this.filteredVerksamhetOptions = verksamhetFormControl.valueChanges
      .startWith(null)
      .map((name: string) => name ? this.filterVerksamhet(name) : this.allVerksamhets.slice());
  }

  private initVardformControl() {
    const vardformFormControl = this.apkForm.get('vardform');

    this.filteredVardformOptions = vardformFormControl.valueChanges
      .startWith(null)
      .map((name: string) => name ? this.filterVardform(name) : this.allVardforms.slice());
  }

  private initAo3FormControl(ao3s) {
    const ao3FormControl = this.apkForm.get('ao3');

    this.filteredAo3Options = ao3FormControl.valueChanges // Side effect
      .startWith(null)
      .map((name: string) => name ? this.filterAo3(name) : this.allAo3s.slice());

    const map: Map<string, Ao3> = new Map();
    for (const ao3 of ao3s) {
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

  private initProdnControls(prodn3: Prodn3) {
    if (prodn3) {
      // We assume the form is already built, so we don't need to fetch the prodn3 again.
      let prodn2;
      let prodn1;

      // Now we know prodn3. Based on that we want to find prodn2, prodn1 as well as options for prodn2 and prodn3.
      // Start by finding the specific prodn2
      // todo Clean up a bit here. Decrease use of Observable.
      Observable.of(prodn3.prodn2)
        .concatMap(prodn2 => {
          // this.apkForm.patchValue({'prodn2': this.getItemInstanceInArray(prodn2, this.prodn2Options)});

          // From this prodn2 we can find the chosen prodn1 but also the options for prodn3.
          const prodn1Observable: Observable<Prodn1> = Observable.of(prodn2.prodn1);

          const prodn3sObservable: Observable<Prodn3[]> = this.http.get('/api/prodn3?prodn2=' + prodn2.id)
            .map(response => response.json())
            .map((pageable: RestResponse<Prodn3[]>) => <Prodn3[]>pageable.content);

          return Observable.forkJoin(prodn1Observable, prodn3sObservable);
        })
        .concatMap((result: any[]) => {
          const prodn1 = result[0];
          const prodn3s = result[1];

          this.prodn3Options = prodn3s;

          // Moving on with finding the options for prodn2, based on prodn1
          return this.http.get('/api/prodn2?prodn1=' + prodn1.id);
        })
        .map(response => response.json().content)
        .subscribe(prodn2s => {
          this.prodn2Options = prodn2s;

          // Need to get the exact instance to make the component find a match and be able to display an option from the option list.
          this.apkForm.patchValue({'prodn2': this.getItemInstanceInArray(prodn3.prodn2, this.prodn2Options)});
          this.apkForm.patchValue({'prodn1': this.getItemInstanceInArray(prodn3.prodn2.prodn1, this.allProdn1s)});
          this.apkForm.patchValue({'prodn3': this.getItemInstanceInArray(prodn3, this.prodn3Options)});

          this.listenToChangesToProdnx();
        });
    } else {
      this.listenToChangesToProdnx();
    }
  }

  private getItemInstanceInArray(item: any, array: any[]) {
    let filtered = array.filter(arrayItem => arrayItem.id === item.id);

    if (filtered.length > 0) {
      return filtered[0];
    } else {
      throw new Error('Entity not found.');
    }
  }

  listenToChangesToProdnx() {
    const prodn1Control = this.apkForm.get('prodn1');
    const prodn2Control = this.apkForm.get('prodn2');

    prodn1Control.valueChanges
      .filter(value => value ? true : false)
      .flatMap(prodn1 =>
        this.http.get('/api/prodn2?prodn1=' + prodn1.id).map(response => response.json().content))
      .subscribe(prodn2s => {
        this.prodn2Options = prodn2s;
        this.prodn3Options = [];
        this.apkForm.patchValue({
          'prodn2': null,
          'prodn3': null
        });
      });

    prodn2Control.valueChanges
      .filter(value => value ? true : false)
      .flatMap(prodn2 => this.http.get('/api/prodn3?prodn2=' + prodn2.id))
      .map(response => response.json())
      .map((pageable: RestResponse<Prodn3[]>) => <Prodn3[]>pageable.content)
      .subscribe(prodn3s => {
        this.prodn3Options = prodn3s;
        this.apkForm.patchValue({
          'prodn3': null
        });
      });
  }

  save() {
    if (!this.apkForm.valid) {
      this.saveMessage = 'Alla fält är inte korrekt ifyllda.';
      return;
    }

    const data = this.data;
    const formModel = this.apkForm.value;
    data.lankod = data.lankod || '14'; // Hard-coded
    data.agarform = formModel.agarform;
    data.groupCode = formModel.groupCode;
    data.ao3 = (<Ao3> formModel.ao3).ao3id;
    data.anmarkning = formModel.anmarkning;
    data.ansvar = formModel.ansvar;
    data.frivilligUppgift = formModel.frivilligUppgift;
    data.hsaid = formModel.hsaid;
    data.vardform = (<Vardform> formModel.vardform).vardformid;
    data.verksamhet = (<Verksamhet> formModel.verksamhet).verksamhetid;
    data.benamning = formModel.benamning;
    if (!formModel.groupCode) {
      data.postadress = formModel.addressGroup.postadress;
      data.postnr = formModel.addressGroup.postnr;
      data.postort = formModel.addressGroup.postort;
    }
    data.externfakturamodell = formModel.externfakturaGroup.externfakturamodell;
    data.prodn1 = formModel.prodn1;
    data.prodn3 = formModel.prodn3;
    data.vgpv = formModel.vgpvGroup.vgpv;
    data.fromDatum = formModel.fromDatum && typeof formModel.fromDatum === 'object' ? formModel.fromDatum.toLocaleDateString() : formModel.fromDatum;
    data.tillDatum = formModel.tillDatum && typeof formModel.tillDatum === 'object' ? formModel.tillDatum.toLocaleDateString() : formModel.tillDatum;

    const headers = new Headers({'Content-Type': 'application/json'});
    const options = new RequestOptions({headers: headers});

    this.http.put('/api/data', JSON.stringify(data), options)
      .map(response => response.json())
      .subscribe((data: Data) => {
        this.data = data;
        this.buildForm();
        this.snackBar.open('Lyckades spara!', null, {duration: 3000});
      });
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
      const parts = unit.attributes.hsaStreetAddress[0].split('$');

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
      'ao3': unit.attributes.vgrAo3kod ? this.ao3IdMap.get(unit.attributes.vgrAo3kod[0]) : null,
      'hsaid': unit.attributes.hsaIdentity ? unit.attributes.hsaIdentity[0] : null,
      'benamning': unit.attributes.ou ? unit.attributes.ou[0] : null,
      'ansvar': unit.attributes.vgrAnsvarsnummer ? unit.attributes.vgrAnsvarsnummer[0] : null,
    });

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
    const datePattern = /^\d{4}-\d{2}-\d{2}$/;

    let value = control.value;

    if (value && typeof value === 'object') {
      value = value.toLocaleDateString();
    }

    if (!value || !value.match(datePattern)) {
      return {'datePattern': true};
    }

    return null;
  }
}
