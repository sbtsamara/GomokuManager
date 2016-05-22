using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace CSBot
{
    class Option
    {
        public Path first { get; set; }
        public Path second { get; set; }
        public int countStep { get; set; }

        public Option(Path first, Path second, int countStep)
        {
            this.first = first;
            this.second = second;
            this.countStep = countStep;
        }
    }
}
