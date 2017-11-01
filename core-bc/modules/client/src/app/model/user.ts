import {Prodn1} from './prodn1';
export class User {
  id: string;
  firstName: string;
  lastName: string;
  mail: string;
  prodn1s: Prodn1[];
  role: string;
  inactivated: boolean;
}
