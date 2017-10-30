let rec break (point : int) (input : int list) (acc : int list) : (int list) * (int list) =
      match input with
      | h :: t -> if h = point then (acc, t) else break point t (acc @ [h])
      | [] -> failwith "False case"
;;

let rec print (l : int list) =
  match l with
  | h :: t -> print_int h; print_endline ""; print t
  | [] -> ()
;;

let x, y = (break 8 [0; 1; 8; 4; 5] []) in print x ; print y ;;