package searchtags

type SearchTag struct {
	UserName UserName
	Key      string
	Value    string
}

// UserName todo have to be moved
type UserName = string
